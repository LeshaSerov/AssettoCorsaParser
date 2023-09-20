package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.domain.championship.Championship;
import org.springframework.data.repository.ListCrudRepository;

public interface ChampionshipRepository extends ListCrudRepository<Championship, Long> {
}
