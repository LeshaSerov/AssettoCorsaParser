package education.AssettoCorsaParser.domain.championship.participant;

import education.AssettoCorsaParser.domain.championship.Championship;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Racer implements Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String url;
    String name;
    String city;
    String country;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "command_id")
    private Team team;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "pilots")
    private Set<Championship> championships = new HashSet<>();

    public Racer parseAndPopulate(Element card) {
        try {

            // Извлекаем имя пользователя
            Element userNameElement = card.select("h1.card-title").first();
            name = userNameElement.text().trim();
            System.out.println(name);

            // Извлекаем команду пользователя
            Element teamElement = card.select("tr:has(td:contains(Команда)) td:eq(1)").first();
            if (teamElement != null) {
                team = new Team();
                team.setTitle(teamElement.text().trim());
            }

        } catch (Exception e) {
            System.out.println(card.baseUri());
            e.printStackTrace();
        }
        return this;
    }
}
