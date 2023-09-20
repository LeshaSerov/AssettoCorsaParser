package education.AssettoCorsaParser.domain.championship;

import org.jsoup.nodes.Element;

public abstract class Result {
    private String title;

    public abstract Result parse(Element card);
}
