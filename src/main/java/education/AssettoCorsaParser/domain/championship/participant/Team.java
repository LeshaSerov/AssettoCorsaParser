package education.AssettoCorsaParser.domain.championship.participant;

import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Team implements Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Racer> racers = new ArrayList<>();

    @Override
    public Team parseAndPopulate(Element card) {
        try {

            // Извлекаем название команды
            Element teamNameElement = card.select("h1.card-title").first();
            title = teamNameElement.text().trim();
            System.out.println(title);

            // Извлекаем состав команды
            Element teamMembersElement = card.select("td.first + td a").first();
            String teamMembers = teamMembersElement.text().trim();

            // Разделяем список по переносу строки и добавляем в список
            String[] members = teamMembers.split("\n");
            for (String member : members) {
                Racer racer = new Racer();
                racer.name = member.trim();
                racers.add(racer);
            }

        } catch (Exception e) {
            System.out.println(card.baseUri());
            e.printStackTrace();
        }
        return this;
    }
}
