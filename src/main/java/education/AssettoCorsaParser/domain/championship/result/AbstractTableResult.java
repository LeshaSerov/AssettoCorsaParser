package education.AssettoCorsaParser.domain.championship.result;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.championship.ResultItem;
import jakarta.persistence.*;
import lombok.*;

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
public abstract class AbstractTableResult implements Parsing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ElementCollection
    private List<String> stageNames = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultItem> results;


}
