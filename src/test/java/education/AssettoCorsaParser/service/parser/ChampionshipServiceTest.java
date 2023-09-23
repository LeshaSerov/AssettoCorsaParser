package education.AssettoCorsaParser.service.parser;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ChampionshipServiceTest {

    private final ChampionshipRepository championshipRepository;

    @Autowired
    public ChampionshipServiceTest(ChampionshipRepository championshipRepository) {
        this.championshipRepository = championshipRepository;
    }
    @Test
    public void fetchDataAndSaveSingleChampionship() {
        try {
            String url = "https://yoklmnracing.ru/championships";
            // Здесь вы можете указать URL для одного конкретного чемпионата, например:
            String singleChampionshipUrl = "https://yoklmnracing.ru/championships/147"; // Замените на реальный URL
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