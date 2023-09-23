package education.AssettoCorsaParser.entity.participant;

import education.AssettoCorsaParser.entity.Parsable;
import education.AssettoCorsaParser.entity.championship.Chart;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@jakarta.persistence.Table(name = "team")
public class Team implements Parsable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NonNull
  @Column(name = "name", unique = true)
  private String name;


  @Builder.Default
  @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Racer> racers = new LinkedHashSet<>();


  @ManyToMany(mappedBy = "teams")
  private Set<Chart> charts = new LinkedHashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Team team = (Team) o;
    return getId() != null && Objects.equals(getId(), team.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}