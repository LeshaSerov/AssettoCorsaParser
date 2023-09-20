package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.participant.Racer;
import education.AssettoCorsaParser.domain.participant.Team;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
@Slf4j
public class TableResult implements Parsing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String url;

    private Boolean isTeamResult = false;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    @OneToMany(mappedBy = "tableResult", cascade = CascadeType.ALL)
    private List<Stage> stages = new ArrayList<>();

    @ManyToMany(mappedBy = "tableResultForRacer", cascade = CascadeType.ALL)
    private List<Racer> racers = new ArrayList<>();

    @ManyToMany(mappedBy = "tableResultForTeam", cascade = CascadeType.ALL)
    private List<Team> teams = new ArrayList<>();
    @ElementCollection
    List<String> result = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RowTable> table;

    @Override
    public TableResult parseAndPopulate(Element card) {
        try {

            for (Element e : card.select("thead th:has(a)")) {
                stages.add(new Stage().parseAndPopulate(Jsoup.connect("https://yoklmnracing.ru" + e.select("a").attr("href")).get()));
            }

            result = card.select("tbody tr td.text-center:last-child").stream()
                    .map(Element::text).toList();

            Element table = card.select("table:has(th:contains(Пилот))").first();
            if (table != null) {
                for (Element e : table.select("td:eq(1)")) {
                    Element element = Jsoup.connect("https://yoklmnracing.ru" + e.select("a").attr("href")).get();
                    if (isTeamResult) {
                        teams.add(new Team().parseAndPopulate(element));
                    } else {
                        racers.add(new Racer().parseAndPopulate(element));
                    }
                }

                List<String> elementsInnerTable = table.select("td.text-center:not(td:last-child)").stream()
                        .map(Element::text)
                        .toList();
                int batchSize = card.select("thead th:has(a)").size();
                this.table = IntStream.range(0, (elementsInnerTable.size() + batchSize - 1) / batchSize)
                        .mapToObj(i -> elementsInnerTable.subList(i * batchSize, Math.min((i + 1) * batchSize, elementsInnerTable.size())))
                        .map(batch -> {
                            RowTable rowTable = new RowTable();
                            rowTable.setRowData(batch);
                            return rowTable;
                        })
                        .toList();
            }

            log.atInfo().log(this.title + " tableResult was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }
        return this;
    }

    @Entity
    @Setter
    public static class RowTable {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ElementCollection
        private List<String> rowData;
    }


}
