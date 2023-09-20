package education.AssettoCorsaParser.domain;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.domain.championship.result.PilotTableResult;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Pilot implements Parsing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String name;
    String surname;
    String city;
    String country;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "command_id")
    private Command command;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "pilots")
    private Set<Championship> championships = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "pilots")
    private Set<PilotTableResult> tableResults = new HashSet<>();

    //https://yoklmnracing.ru/drivers

    public Pilot parseAndPopulate(Element card) {

        return this;
    }
}
