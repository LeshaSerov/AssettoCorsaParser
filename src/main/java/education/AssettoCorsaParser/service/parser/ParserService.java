package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.domain.participant.Racer;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import education.AssettoCorsaParser.repository.championship.StageRepository;
import education.AssettoCorsaParser.repository.championship.TableResultRepository;
import education.AssettoCorsaParser.repository.participant.RacerRepository;
import education.AssettoCorsaParser.repository.participant.TeamRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
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
public class ParserService {

    private final ChampionshipRepository championshipRepository;
    private final TableResultRepository tableResultRepository;
    private final StageRepository stageRepository;
    private final RacerRepository racerRepository;
    private final TeamRepository teamRepository;

//    @Scheduled(fixedRate = 3600000) // Запуск каждый час (3600000 миллисекунд)
    @Transactional
    public void fetchDataAndSave() {
        try {
            log.atInfo().log("Start parsing site!");
            String url = "https://yoklmnracing.ru/championships";
            // Получение HTML-страницы с сайта
            Document baseDoc = Jsoup.connect(url).get();
            // Поиск всех элементов с классом "card mb-3", которые содержат информацию о чемпионатах
            Elements championshipCards = baseDoc.select("div.card.mb-3");

            List<Championship> championshipList = new ArrayList<>();
            // Перебор всех найденных элементов
            for (Element element : championshipCards) {
                championshipList.add(new Championship().parseAndPopulate(Jsoup.connect(url + "/" + getId(element)).get()));
            }

            championshipRepository.saveAll(championshipList);
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
