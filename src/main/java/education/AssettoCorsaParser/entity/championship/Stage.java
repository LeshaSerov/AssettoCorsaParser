package education.AssettoCorsaParser.entity.championship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import education.AssettoCorsaParser.entity.Parsable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@jakarta.persistence.Table(name = "stage")
public class Stage implements Parsable {

  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Include
  @Column(name = "internal_id")
  private Integer internalId;

  @Include
  @Column(name = "title")
  private String title;

  @Include
  @Column(name = "date")
  private String date;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "chart_id")
  private Chart chart;
}