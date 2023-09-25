package education.AssettoCorsaParser.service;

import education.AssettoCorsaParser.entity.championship.Stage;
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

  @Override
  public Stage parse(Element card) {
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
    Stage stage = Stage.builder()
        .internalId(internalId)
        .title(title)
        .date(date)
        .build();
    log.atInfo().log("      " + title + " - Stage was successfully parsed");
    return stage;
  }


}
