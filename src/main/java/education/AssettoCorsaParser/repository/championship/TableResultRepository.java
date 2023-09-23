package education.AssettoCorsaParser.repository.championship;

import education.AssettoCorsaParser.domain.championship.TableResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface TableResultRepository extends JpaRepository<TableResult, Long> {
}
