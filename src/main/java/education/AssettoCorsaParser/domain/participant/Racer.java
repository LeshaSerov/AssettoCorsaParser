package education.AssettoCorsaParser.domain.participant;

import education.AssettoCorsaParser.domain.championship.Championship;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode(callSuper = false)
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

    @Column(name = "profile_url")
    private String profileUrl;

    private String name;
    private String city;
    private String country;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToMany(mappedBy = "pilots")
    private Set<Championship> championships = new HashSet<>();

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

            Element teamElement = card.select("tr:has(td:contains(Команда)) td:eq(1)").first();
            if (teamElement != null) {
                team = new Team();
                team.setName(teamElement.text().trim());
            }

            log.atInfo().log(this.name + " Racer was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }
        return this;
    }
}
