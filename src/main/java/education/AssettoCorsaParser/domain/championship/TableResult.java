package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.championship.participant.Participant;
import education.AssettoCorsaParser.domain.championship.participant.Racer;
import education.AssettoCorsaParser.domain.championship.participant.Team;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
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
    @OneToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany
    private List<Stage> stages = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
//    private List<Participant> participating = new ArrayList<>();
    private TreeMap<Participant, String> totalResult = new TreeMap<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RowTable> table;

    @Override
    public TableResult parseAndPopulate(Element card) {
        try {

            System.out.println(title);

            Element table = card.select("table:has(th:contains(Пилот))").first(); // Выбираем первую таблицу

            // Парсинг верхнего заголовка
            for (Element e : card.select("thead th:has(a)")) {
                stages.add(new Stage().parseAndPopulate(Jsoup.connect("https://yoklmnracing.ru" + e.select("a").attr("href")).get()));
            }

            // Парсинг бокового заголовка - Участники
            List<Participant> participants = new ArrayList<>();
            for (Element e : table.select("td:eq(1)")) {
                Participant participant = isTeamResult ? new Team() : new Racer();
                participants.add(participant.parseAndPopulate(Jsoup.connect("https://yoklmnracing.ru" + e.select("a").attr("href")).get()));
            }
            // Парсинг итоговой колонки - Результаты
            List<String> totalColumnList = card.select("tbody tr td.text-center:last-child").stream()
                    .map(Element::text).toList();
            // Итоговая мапа с результами чемпионата
            for (int i = 0; i < participants.size(); i++) {
                totalResult.put(participants.get(i), totalColumnList.get(i));
            }

            // Парсинг внутренней таблицы
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

        } catch (Exception ignored) {
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

//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ResultItem> table;


/*    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "command_table_result",
            joinColumns = @JoinColumn(name = "command_table_result_id"),
            inverseJoinColumns = @JoinColumn(name = "command_id")
    )
    private List<Command> commands = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "pilot_table_result",
            joinColumns = @JoinColumn(name = "pilot_table_result_id"),
            inverseJoinColumns = @JoinColumn(name = "pilot_id")
    )
    private List<Pilot> pilots = new ArrayList<>();*/
