package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.entity.championship.Chart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartRepository extends JpaRepository<Chart, Long> {
  Optional<Chart> findByUrl(String url);
}
