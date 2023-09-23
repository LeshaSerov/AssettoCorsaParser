package education.AssettoCorsaParser.service.builder;

import education.AssettoCorsaParser.entity.championship.Stage;
import education.AssettoCorsaParser.repository.championship.StageRepository;
import education.AssettoCorsaParser.service.ParsingService;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StageService implements ParsingService {

  private final StageRepository stageRepository;

  @Override
  public Stage parse(Element card) {

    Stage existingStage = null;

    String title = card.select("h1.card-title").text();
    Integer internalId = null;
    {
      Element linkElement = card.select("link[rel=canonical]").first();
      if (linkElement != null) {
        String href = linkElement.attr("href");
        Pattern pattern = Pattern.compile("/(\\d+)$");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
          internalId = Integer.parseInt(matcher.group(1));
          Optional<Stage> optionalStage = stageRepository.findByTitle(title);
          if (optionalStage.isPresent()) {
            existingStage = optionalStage.get();
          }
        }
      }
    }

    String date = "";
    {
      String dateText = card.select("h4").text();
      if (dateText.isEmpty()) {
        String startDate = card.select("tr:has(td:contains(Начало:)) td.text-end")
            .get(1).select("div.d-none.d-sm-block").text();
        String endDate = card.select("tr:has(td:contains(Завершение:)) td.text-end")
            .get(1).select("div.d-none.d-sm-block").text();
        dateText = startDate + " - " + endDate;
      }
      date = dateText;
    }

    if (existingStage == null) {
      Stage stage = Stage.builder()
          .internalId(internalId)
          .title(title)
          .date(date)
          .build();
      log.atInfo().log(title + " - Stage was successfully parsed");
      stageRepository.save(stage);
      log.atInfo().log(title + " - Stage was successfully saved");
      return stage;
    } else {
      existingStage.setInternalId(internalId);
      existingStage.setTitle(title);
      existingStage.setDate(date);
      return existingStage;
    }
  }


}
