package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        System.out.println(card.text());
        this.internalId = Integer.parseInt(Objects.requireNonNull(card.select("td.first.text-end").first()).text());
        this.title = card.select("h1.card-title").text().trim();
        this.date = card.text();
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
