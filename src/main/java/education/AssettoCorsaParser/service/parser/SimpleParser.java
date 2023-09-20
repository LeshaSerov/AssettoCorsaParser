package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.domain.championship.Championship;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SimpleParser {

    static public void parse() {
        try {
            // URL сайта, который нужно спарсить
            String url = "https://yoklmnracing.ru/championships";

            // Получение HTML-страницы с сайта
            Document baseDoc = Jsoup.connect(url).get();

            // Поиск всех элементов с классом "card mb-3", которые содержат информацию о чемпионатах
            Elements championshipCards = baseDoc.select("div.card.mb-3");

            // Перебор всех найденных элементов
            for (Element element : championshipCards) {
                System.out.println(new Championship().parseAndPopulate(Jsoup.connect(url + "/" + getId(element)).get()).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private String getId(Element element)
    {
        return element.selectFirst("h1.card-title.float-start a").attr("href").replaceAll("\\D", "");
    }



}
