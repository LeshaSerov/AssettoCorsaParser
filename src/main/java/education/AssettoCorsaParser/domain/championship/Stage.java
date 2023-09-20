package education.AssettoCorsaParser.domain.championship;

import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer internalId;
    private String title;
    private String schedule;
    private LocalDate beginDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    //https://yoklmnracing.ru/championships/147
    public static Stage parse(Element card) {
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
    }
}
