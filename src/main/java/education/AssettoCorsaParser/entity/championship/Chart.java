package education.AssettoCorsaParser.entity.championship;

import education.AssettoCorsaParser.entity.Parsable;
import education.AssettoCorsaParser.entity.participant.Racer;
import education.AssettoCorsaParser.entity.participant.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
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
@Table(name = "chart")
public class Chart implements Parsable {

  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Include
  @NonNull
  @Column(name = "title", unique = true)
  private String title;

  @Include
  @NonNull
  @Column(name = "url", unique = true)
  private String url;

  @Include
  @Column(name = "is_team")
  private Boolean isTeam;

  @Include
  @ElementCollection
  @Column(name = "result")
  @CollectionTable(name = "chart_result", joinColumns = @JoinColumn(name = "owner_id"))
  private List<String> result = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "championship_id")
  private Championship championship;

  @Builder.Default
  @OneToMany(mappedBy = "chart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Row> rows = new ArrayList<>();


  @Builder.Default
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "chart_racers",
      joinColumns = @JoinColumn(name = "chart_id"),
      inverseJoinColumns = @JoinColumn(name = "racers_id"))
  private Set<Racer> racers = new LinkedHashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "chart_stages",
      joinColumns = @JoinColumn(name = "chart_id"),
      inverseJoinColumns = @JoinColumn(name = "stages_id"))
  private Set<Stage> stages = new LinkedHashSet<>();

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "chart_teams",
      joinColumns = @JoinColumn(name = "chart_id"),
      inverseJoinColumns = @JoinColumn(name = "teams_id"))
  private Set<Team> teams = new LinkedHashSet<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Chart chart = (Chart) o;
    return getId() != null && Objects.equals(getId(), chart.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}