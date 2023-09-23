package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChampionshipService {

  private final ChampionshipRepository championshipRepository;

  @Autowired
  public ChampionshipService(ChampionshipRepository championshipRepository) {
    this.championshipRepository = championshipRepository;
  }

  @Scheduled(fixedRate = 1000000)
  public void fetchDataAndSaveSingleChampionship() {
    try {
      // Здесь вы можете указать URL для одного конкретного чемпионата, например:
      String singleChampionshipUrl = "https://yoklmnracing.ru/championships/54"; // Замените на реальный URL
      // Получение HTML-страницы с сайта
      Document singleChampionshipDoc = Jsoup.connect(singleChampionshipUrl).get();
      // Создание объекта чемпионата и его парсинг
      Championship championship = new Championship().parseAndPopulate(singleChampionshipDoc);
      // Сохранение чемпионата в репозитории
      championshipRepository.save(championship);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}