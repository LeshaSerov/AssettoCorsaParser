package education.AssettoCorsaParser.domain.participant;

import education.AssettoCorsaParser.domain.championship.TableResult;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class Team extends Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Racer> racers = new ArrayList<>();

    @ManyToMany(mappedBy = "teams")
    private Set<TableResult> results = new HashSet<>();


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

            log.atInfo().log("    " + this.name + " - Team was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }
        return this;
    }
}
