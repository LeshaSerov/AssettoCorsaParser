package education.AssettoCorsaParser.repository.participant;

import education.AssettoCorsaParser.domain.participant.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
