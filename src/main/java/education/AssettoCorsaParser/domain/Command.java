package education.AssettoCorsaParser.domain;

import jakarta.persistence.*;
import lombok.*;

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
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String title;

    @OneToMany(mappedBy = "command", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pilot> pilots = new ArrayList<>();
}
