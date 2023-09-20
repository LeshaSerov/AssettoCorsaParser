package education.AssettoCorsaParser.domain.championship.result;

import education.AssettoCorsaParser.domain.Command;
import education.AssettoCorsaParser.domain.Pilot;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Entity
public class CommandTableResult extends AbstractTableResult {

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "command_table_result",
            joinColumns = @JoinColumn(name = "command_table_result_id"),
            inverseJoinColumns = @JoinColumn(name = "command_id")
    )
    private List<Command> commands = new ArrayList<>();
    @Override
    public CommandTableResult parseAndPopulate(Element card) {
        return null;
    }
}
