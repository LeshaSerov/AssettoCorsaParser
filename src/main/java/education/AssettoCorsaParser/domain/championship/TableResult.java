package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.participant.Racer;
import education.AssettoCorsaParser.domain.participant.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

@Entity
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Championship championship;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableResult", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Stage> stages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tableResults_teams", joinColumns = @JoinColumn(name = "tableResult_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tableResults_racers", joinColumns = @JoinColumn(name = "tableResult_id"), inverseJoinColumns = @JoinColumn(name = "racer_id"))
    private List<Racer> racers = new ArrayList<>();


    @ElementCollection
    List<String> result = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableResult", orphanRemoval = true, cascade = CascadeType.ALL)
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
                    Element element = Jsoup.connect(
                        "https://yoklmnracing.ru" + e.select("a").attr("href")).get();
                    if (isTeamResult) {
                        teams.add(new Team().parseAndPopulate(element));
                    } else {
                        racers.add(new Racer().parseAndPopulate(element));
                    }
                }

                List<String> elementsInnerTable = table.select("td.text-center:not(td:last-child)")
                    .stream().map(Element::text).toList();
                int batchSize = card.select("thead th:has(a)").size();
                this.table = IntStream.range(0,
                    (elementsInnerTable.size() + batchSize - 1) / batchSize).mapToObj(
                    i -> elementsInnerTable.subList(i * batchSize,
                        Math.min((i + 1) * batchSize, elementsInnerTable.size()))).map(batch -> {
                    RowTable rowTable = new RowTable();
                    rowTable.setRowData(batch);
                    return rowTable;
                }).toList();
            }

            for (Stage stage : stages) {
                stage.setTableResult(this);
            }
            for (Team team : teams) {
                team.getResults().add(this);
            }
            for (Racer racer : racers) {
                racer.getResults().add(this);
            }
            for (RowTable rowTable : this.table) {
                rowTable.setTableResult(this);
            }

            log.atInfo().log("  " + this.title + " - TableResult was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }
        return this;
    }


}
