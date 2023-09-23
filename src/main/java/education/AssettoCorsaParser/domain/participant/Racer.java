package education.AssettoCorsaParser.domain.participant;

import education.AssettoCorsaParser.domain.championship.TableResult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter(value = AccessLevel.PACKAGE)
@Table
@ToString
@Slf4j
public class Racer extends Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String profileUrl;

    private String name;
    private String city;
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @ManyToMany(mappedBy = "racers")
    private Set<TableResult> results = new HashSet<>();

    public Racer parseAndPopulate(Element card) {
        try {
            Element profileUrlElement = card.select("h1.card-title a").first();
            if (profileUrlElement != null) {
                profileUrl = profileUrlElement.attr("href");
            }

            Element userNameElement = card.select("h1.card-title").first();
            if (userNameElement != null) {
                name = userNameElement.text().trim();
            }

            Element cityElement = card.select("tr:has(td:contains(Город)) td:eq(1)").first();
            if (cityElement != null) {
                city = cityElement.text().trim();
            }

            Element countryElement = card.select("tr:has(td:contains(Страна)) td:eq(1)").first();
            if (countryElement != null) {
                country = countryElement.text().trim();
            }

            log.atInfo().log("    " + this.name + " - Racer was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }
        return this;
    }
}
