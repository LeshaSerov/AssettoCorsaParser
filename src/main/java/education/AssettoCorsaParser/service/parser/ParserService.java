package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.entity.championship.Championship;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import education.AssettoCorsaParser.service.builder.ChampionshipService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParserService {

    private final ChampionshipRepository championshipRepository;

    private final ChampionshipService championshipService;


//    @Scheduled(fixedRate = 3600000) // Запуск каждый час (3600000 миллисекунд)
//    @Transactional
//    public void fetchDataAndSave() {
//        try {
//            log.atInfo().log("Start parsing site!");
//            String url = "https://yoklmnracing.ru/championships";
//            // Получение HTML-страницы с сайта
//            Document baseDoc = Jsoup.connect(url).get();
//            // Поиск всех элементов с классом "card mb-3", которые содержат информацию о чемпионатах
//            Elements championshipCards = baseDoc.select("div.card.mb-3");
//
//            List<Championship> championshipList = new ArrayList<>();
//            // Перебор всех найденных элементов
//            for (Element element : championshipCards) {
//                championshipList.add(championshipService.parse(Jsoup.connect(url + "/" + getId(element)).get()));
//            }
//
//            championshipRepository.saveAll(championshipList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Transactional
//    @PostConstruct
//    public void fetchData() {
//        try {
//            log.atInfo().log("Start parsing site!");
//            String url = "https://yoklmnracing.ru/championships/147";
////            54
//            // Получение HTML-страницы с сайта
//            Document baseDoc = Jsoup.connect(url).get();
//            Championship championship = championshipService.parse(baseDoc);
//        } catch (IOException e) {
////            ОШИБКА: значение NULL в столбце "owner_id" отношения "chart" нарушает ограничение NOT NULL
////            Подробности: Ошибочная строка содержит (f, 1, 1, null, null, ЕвроЛига, https://yoklmnracing.ru/championships/147?tab=standings&tier=100...).
//            e.printStackTrace();
//        }
//    }

    static private String getId(Element element) {
        Element e = element.selectFirst("h1.card-title.float-start a");
        if (e == null) {
            return "";
        } else {
            return e.attr("href").replaceAll("\\D", "");
        }
    }
}
