package education.AssettoCorsaParser.domain.championship.participant;

import education.AssettoCorsaParser.domain.Parsing;
import org.jsoup.nodes.Element;

public interface Participant extends Parsing {

    @Override
    Participant parseAndPopulate(Element card);
}
