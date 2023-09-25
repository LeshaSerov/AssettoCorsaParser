package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.entity.championship.Championship;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import education.AssettoCorsaParser.service.ChampionshipService;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SuperParserService {

  private final ChampionshipService championshipService;
  private final ChampionshipRepository championshipRepository;

  private List<Element> elementList;
  private final String url = "https://yoklmnracing.ru/championships";

  @PostConstruct
  public void parsing() {
    try {
      // Получение HTML-страницы с сайта
      Document baseDoc = Jsoup.connect(url).get();
      // Поиск всех элементов с классом "card mb-3", которые содержат информацию о чемпионатах
      elementList = baseDoc.select("div.card.mb-3");
      // Перебор всех найденных элементов
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Scheduled(fixedRate = 300000)
  private void parseElement() {
    if (elementList.isEmpty()) {
      parsing();
    } else {
      try {
        Element element = elementList.get(0);
        log.atInfo().log("Start parsing " + url + "/" + getId(element));
        Championship championship =
            championshipService.parse(Jsoup.connect(url + "/" + getId(element)).get());
        championshipRepository.save(championship);
        elementList.remove(element);
        log.atInfo().log("Parsing element successfully!");
      } catch (IOException e) {
        e.printStackTrace();
      }
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
