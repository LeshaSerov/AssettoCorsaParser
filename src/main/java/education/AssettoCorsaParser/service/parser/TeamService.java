package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import education.AssettoCorsaParser.repository.participant.TeamRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TeamService {

  private final TeamRepository teamRepository;

  @Autowired
  public TeamService(TeamRepository repository) {
    this.teamRepository = repository;
  }

  @Scheduled(fixedRate = 1000000)
  public void fetchDataAndSaveAllTeam() {
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


}