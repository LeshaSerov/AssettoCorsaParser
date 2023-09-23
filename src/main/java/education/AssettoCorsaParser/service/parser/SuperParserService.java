package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.entity.championship.Championship;
import education.AssettoCorsaParser.service.builder.ChampionshipService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuperParserService {

  private final ChampionshipService championshipService;

  @Scheduled(fixedRate = 10000) // Запуск каждый час (3600000 миллисекунд)
  @Transactional
  public void fetchDataAndSave() {
    try {
      log.atInfo().log("Start parsing site!");
      String url = "https://yoklmnracing.ru/championships";
      // Получение HTML-страницы с сайта
      Document baseDoc = Jsoup.connect(url).get();
      // Поиск всех элементов с классом "card mb-3", которые содержат информацию о чемпионатах
      Elements championshipCards = baseDoc.select("div.card.mb-3");

      // Перебор всех найденных элементов
      for (Element element : championshipCards) {
        championshipService.parse(Jsoup.connect(url + "/" + getId(element)).get());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
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
