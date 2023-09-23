package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.domain.championship.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface StageRepository extends JpaRepository<Stage, Long> {
}
