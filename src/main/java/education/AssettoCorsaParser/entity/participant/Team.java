package education.AssettoCorsaParser.entity.participant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import education.AssettoCorsaParser.entity.Parsable;
import education.AssettoCorsaParser.entity.championship.Chart;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Include;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@jakarta.persistence.Table(name = "team")
public class Team implements Parsable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "chart_id")
  private Chart chart;

  @Include
  @Builder.Default
  @ElementCollection
  @Column(name = "racer")
  @CollectionTable(name = "team_racers", joinColumns = @JoinColumn(name = "owner_id"))
  private Set<String> racers = new LinkedHashSet<>();
}