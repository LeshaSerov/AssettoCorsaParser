package education.AssettoCorsaParser.domain.participant;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter(value = AccessLevel.PACKAGE)
@Table
@ToString
@Slf4j
public class Team extends Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "team_name")
    private String name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Racer> racers = new ArrayList<>();

    @Override
    public Team parseAndPopulate(Element card) {
        try {
            Element teamNameElement = card.select("h1.card-title").first();
            if (teamNameElement != null) {
                name = teamNameElement.text().trim();
            }

            Element teamMembersElement = card.select("td.first + td a").first();
            if (teamMembersElement != null) {
                String teamMembers = teamMembersElement.text().trim();

                String[] members = teamMembers.split("\n");
                for (String member : members) {
                    Racer racer = new Racer();
                    racer.setName(member.trim());
                    racers.add(racer);
                }
            }

            log.atInfo().log(this.name + " Team was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }
        return this;
    }
}
