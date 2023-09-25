package education.AssettoCorsaParser.entity.participant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import education.AssettoCorsaParser.entity.Parsable;
import education.AssettoCorsaParser.entity.championship.Chart;
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
@jakarta.persistence.Table(name = "racer")
public class Racer implements Parsable {

  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Include
  @Column(name = "url")
  private String url;

  @Include
  @Column(name = "name")
  private String name;

  @Include
  @Column(name = "city")
  private String city;

  @Include
  @Column(name = "country")
  private String country;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "chart_id")
  private Chart chart;
}