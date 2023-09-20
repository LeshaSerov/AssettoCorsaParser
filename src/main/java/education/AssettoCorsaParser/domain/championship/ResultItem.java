package education.AssettoCorsaParser.domain.championship;

import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class ResultItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rowIndex;
    private int columnIndex;
    private String value;

    @ManyToOne
    @JoinColumn(name = "result_championship_id")
    private PilotResult resultChampionship;
}