package education.AssettoCorsaParser.entity.championship;

import education.AssettoCorsaParser.entity.Parsable;
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
@jakarta.persistence.Table(name = "stage")
public class Stage implements Parsable {

  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Include
  @Column(name = "internal_id", unique = true)
  private Integer internalId;

  @Include
  @NonNull
  @Column(name = "title")
  private String title;

  @Include
  @Column(name = "date")
  private String date;

  @ManyToMany(mappedBy = "stages")
  private Set<Chart> charts = new LinkedHashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Stage stage = (Stage) o;
    return getId() != null && Objects.equals(getId(), stage.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}