package education.AssettoCorsaParser.domain;

import org.jsoup.nodes.Element;

public interface Parsing {
    Parsing parseAndPopulate(Element card);

}
