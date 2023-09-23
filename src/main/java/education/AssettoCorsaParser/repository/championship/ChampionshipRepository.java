package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.domain.championship.Championship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampionshipRepository extends JpaRepository<Championship, Long> {


}
