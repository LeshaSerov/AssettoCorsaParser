package education.AssettoCorsaParser.service;

import education.AssettoCorsaParser.entity.Parsable;
import org.jsoup.nodes.Element;
import org.springframework.transaction.annotation.Transactional;

public interface ParsingService {
    @Transactional
    Parsable parse(Element card);

}
