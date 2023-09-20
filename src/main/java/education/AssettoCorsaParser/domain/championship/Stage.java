package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String date;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "tableresult_id")
    private TableResult tableResult;

    @Override
    public Stage parseAndPopulate(Element card) {
        try {
            Element linkElement = card.select("link[rel=canonical]").first();
            if (linkElement != null) {
                String href = linkElement.attr("href");
                Pattern pattern = Pattern.compile("/(\\d+)$");
                Matcher matcher = pattern.matcher(href);
                if (matcher.find()) {
                    this.internalId = Integer.parseInt(matcher.group(1));
                }
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
