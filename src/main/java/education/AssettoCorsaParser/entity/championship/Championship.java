package education.AssettoCorsaParser.entity.championship;

import education.AssettoCorsaParser.entity.Parsable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
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
@jakarta.persistence.Table(name = "championship")
public class Championship implements Parsable {

  @Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Include
  @Column(name = "internal_id")
  private Integer internalId;

  @Include
  @Column(name = "name")
  private String name;

  @Include
  @Column(name = "status")
  private String status;

  @Include
  @Column(name = "simulator")
  private String simulator;

  @Include
  @Column(name = "organization")
  private String organization;

  @Include
  @Column(name = "begin_date")
  private String beginDate;

  @Include
  @Column(name = "end_date")
  private String endDate;

  @Builder.Default
  @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "championship", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(FetchMode.JOIN)
  private List<Chart> charts = new ArrayList<>();
}