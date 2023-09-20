package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.util.Objects;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Stage implements Parsing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer internalId;
    private String title;
    private String schedule;
    private String date;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    //https://yoklmnracing.ru/championships/147
    @Override
    public Stage parseAndPopulate(Element card) {
        try {

            this.internalId = Integer.parseInt(Objects.requireNonNull(card.select("td.first.text-end").first()).text());
            this.title = card.select("h1.card-title").text();
            System.out.println(title);
            String date = card.select("h4").text();
            if (date.equals("")) {
                // Извлекаем данные о начале мероприятия
                String startDate = card.select("tr:has(td:contains(Начало:)) td.text-end")
                        .get(1).select("div.d-none.d-sm-block").text();
                // Извлекаем данные о завершении мероприятия
                String endDate = card.select("tr:has(td:contains(Завершение:)) td.text-end")
                        .get(1).select("div.d-none.d-sm-block").text();
                // Формируем строку с датами в нужном формате
                date = startDate + " - " + endDate;
            }
            this.date = date;
        } catch (Exception e) {
            System.out.println(card.baseUri());
            e.printStackTrace();
        }
        return this;
    }

/*    public Stage parseAndPopulate(Element card) {
        return Stage.builder()
                .internalId(Integer.parseInt(Objects.requireNonNull(card.select("h1.card-title a").first())
                        .attr("href").replaceAll("\\D", ""))
                )
                .title(card.select("h1.card-title").text().trim())
                .beginDate(LocalDate.parse(card.select("td:has(i.fab.fa-solid.fa-flag) + td").text().trim(),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)))
                .endDate(LocalDate.parse(card.select("td:has(i.fab.fa-solid.fa-flag-checkered) + td").text().trim(),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)))
//                .specialSections(card.select("div.table-responsive table tbody tr").stream()
//                        .collect(Collectors.toMap(
//                                section -> section.select("td.first").text().trim(),
//                                section -> "",
//                                (existing, replacement) -> existing,
//                                TreeMap::new
//                        ))
//                )
                .build();

        return this;
    }*/
}
