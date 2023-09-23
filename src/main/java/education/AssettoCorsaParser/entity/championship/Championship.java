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
  @NonNull
  @Column(name = "internal_id", unique = true)
  private Integer internalId;

  @Include
  @NonNull
  @Column(name = "name", unique = true)
  private String name;

  @Include
  @NonNull
  @Column(name = "status")
  private String status;

  @Include
  @NonNull
  @Column(name = "simulator")
  private String simulator;

  @Include
  @NonNull
  @Column(name = "organization")
  private String organization;

  @Include
  @Column(name = "begin_date")
  private LocalDate beginDate;

  @Include
  @Column(name = "end_date")
  private LocalDate endDate;

  @Builder.Default
  @OneToMany(mappedBy = "championship", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Chart> charts = new ArrayList<>();

}