package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.domain.championship.Championship;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parse() throws IOException {
        String url = "https://yoklmnracing.ru/championships";
        // Получение HTML-страницы с сайта
        Document baseDoc = Jsoup.connect(url).get();
        // Поиск всех элементов с классом "card mb-3", которые содержат информацию о чемпионатах
        Elements championshipCards = baseDoc.select("div.card.mb-3");

        List<Championship> championshipList = new ArrayList<>();
        // Перебор всех найденных элементов
//        for (Element element : championshipCards) {
            championshipList.add(new Championship().parseAndPopulate(Jsoup.connect(url + "/" + getId(championshipCards.get(0))).get()));
//        }
        System.out.println(championshipList);
    }

    static private String getId(Element element) {
        Element e = element.selectFirst("h1.card-title.float-start a");
        if (e == null) {
            return "";
        } else {
            return e.attr("href").replaceAll("\\D", "");
        }
    }
}