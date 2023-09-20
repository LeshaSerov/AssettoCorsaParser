package education.AssettoCorsaParser.domain.championship.result;

import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.Pilot;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PilotTableResult extends AbstractTableResult {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "pilot_table_result",
            joinColumns = @JoinColumn(name = "pilot_table_result_id"),
            inverseJoinColumns = @JoinColumn(name = "pilot_id")
    )
    private List<Pilot> pilots = new ArrayList<>();

    @Override
    public Parsing parseAndPopulate(Element card) {

        return this;
    }

}
