package education.AssettoCorsaParser.entity.championship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Include;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
  @Column(name = "title")
  private String title;

  @Include
  @Column(name = "url")
  private String url;

  @Include
  @Column(name = "is_team")
  private Boolean isTeam;

  @Include
  @Builder.Default
  @ElementCollection
  @Column(name = "result")
  @CollectionTable(name = "chart_result", joinColumns = @JoinColumn(name = "owner_id"))
  private List<String> result = new ArrayList<>();

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "championship_id")
  @Fetch(FetchMode.JOIN)
  private Championship championship;

  @Builder.Default
  @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "chart", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  private List<Row> rows = new ArrayList<>();

  @Builder.Default
  @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "chart", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  private List<Stage> stages = new ArrayList<>();

  @Builder.Default
  @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "chart", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  private List<Racer> racers = new ArrayList<>();

  @Builder.Default
  @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "chart", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  private List<Team> teams = new ArrayList<>();
}