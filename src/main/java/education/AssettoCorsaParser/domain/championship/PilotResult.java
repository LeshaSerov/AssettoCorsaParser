package education.AssettoCorsaParser.domain.championship;

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
public class PilotResult extends Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    @ElementCollection
    private List<String> pilotNames = new ArrayList<>();

    @ElementCollection
    private List<String> stageNames = new ArrayList<>();

    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultItem> results;




}
