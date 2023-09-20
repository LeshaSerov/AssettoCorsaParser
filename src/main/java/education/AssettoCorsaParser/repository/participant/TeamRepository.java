package education.AssettoCorsaParser.repository.participant;

import education.AssettoCorsaParser.domain.participant.Team;
import org.springframework.data.repository.ListCrudRepository;

public interface TeamRepository extends ListCrudRepository<Team, Long> {
}
