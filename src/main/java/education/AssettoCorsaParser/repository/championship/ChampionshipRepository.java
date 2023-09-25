package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.entity.championship.Championship;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampionshipRepository extends JpaRepository<Championship, Long> {
  Optional<Championship> findByInternalId(Integer internalId);
}
