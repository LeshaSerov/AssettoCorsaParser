package education.AssettoCorsaParser.repository.participant;

import education.AssettoCorsaParser.domain.participant.Racer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface RacerRepository extends JpaRepository<Racer, Long> {
}
