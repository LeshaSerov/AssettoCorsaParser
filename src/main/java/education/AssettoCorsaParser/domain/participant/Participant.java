package education.AssettoCorsaParser.domain.participant;

import education.AssettoCorsaParser.domain.Parsing;
import org.jsoup.nodes.Element;

public abstract class Participant implements Parsing {

    @Override
    public abstract Participant parseAndPopulate(Element card);
}
