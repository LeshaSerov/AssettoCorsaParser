package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
@Slf4j
public class Stage implements Parsing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer internalId;
    private String title;
    private String schedule;
    private String date;


    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    @Override
    public Stage parseAndPopulate(Element card) {
        try {
            Element firstElement = card.select("td.first.text-end").first();
            if (firstElement != null) {
                this.internalId = Integer.parseInt(firstElement.text());
            }
            this.title = card.select("h1.card-title").text();

            String dateText = card.select("h4").text();
            if (dateText.isEmpty()) {
                String startDate = card.select("tr:has(td:contains(Начало:)) td.text-end")
                        .get(1).select("div.d-none.d-sm-block").text();
                String endDate = card.select("tr:has(td:contains(Завершение:)) td.text-end")
                        .get(1).select("div.d-none.d-sm-block").text();
                dateText = startDate + " - " + endDate;
            }
            this.date = dateText;

            log.atInfo().log(this.title + " stage was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }

        return this;
    }
}
