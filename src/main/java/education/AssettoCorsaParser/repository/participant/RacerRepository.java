package education.AssettoCorsaParser.repository.participant;

import education.AssettoCorsaParser.entity.participant.Racer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RacerRepository extends JpaRepository<Racer, Long> {
  Optional<Racer> findByUrl(String url);
  Optional<Racer> findByName(String name);
}
