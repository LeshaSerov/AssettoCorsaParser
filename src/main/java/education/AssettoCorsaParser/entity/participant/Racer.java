package education.AssettoCorsaParser.entity.participant;

import education.AssettoCorsaParser.entity.Parsable;
import education.AssettoCorsaParser.entity.championship.Chart;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Include;
import org.hibernate.Hibernate;

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
  @NonNull
  @Column(name = "name", unique = true)
  private String name;

  @Include
  @Column(name = "city")
  private String city;

  @Include
  @Column(name = "country")
  private String country;

  @Builder.Default
  @ManyToMany(mappedBy = "racers")
  private Set<Chart> charts = new LinkedHashSet<>();

  @ManyToOne
  @JoinColumn(name = "team_id")
  private Team team;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Racer racer = (Racer) o;
    return getId() != null && Objects.equals(getId(), racer.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}