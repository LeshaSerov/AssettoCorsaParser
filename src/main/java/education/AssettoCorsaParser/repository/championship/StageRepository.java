package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.entity.championship.Stage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
  Optional<Stage> findByTitle(String title);
}
