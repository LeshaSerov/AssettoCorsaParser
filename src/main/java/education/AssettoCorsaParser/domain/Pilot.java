package education.AssettoCorsaParser.domain;

import education.AssettoCorsaParser.domain.championship.Championship;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String name;
    String surname;
    String city;
    String country;

    @ManyToOne
    @JoinColumn(name = "command_id")
    private Command command;

    @ManyToMany(mappedBy = "pilots")
    private List<Championship> championships = new ArrayList<>();

    //https://yoklmnracing.ru/drivers
    public static Pilot parse(Element card) {

    }
}
