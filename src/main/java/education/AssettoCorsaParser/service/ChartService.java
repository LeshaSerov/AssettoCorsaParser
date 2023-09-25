package education.AssettoCorsaParser.service;

import education.AssettoCorsaParser.entity.championship.Chart;
import education.AssettoCorsaParser.entity.championship.Row;
import education.AssettoCorsaParser.entity.championship.Stage;
import education.AssettoCorsaParser.entity.participant.Racer;
import education.AssettoCorsaParser.entity.participant.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChartService implements ParsingService {
  private final StageService stageService;
  private final TeamService teamService;
  private final RacerService racerService;

  @Override
  public Chart parse(Element card) {
    log.atInfo().log("    Chart parsing");
    String title;
    String url = card.baseUri();
    boolean isTeam = false;
    {
      if (card.getElementById("tier-select") == null) {
        title = "Личный";
      } else {
        try {
          Element e = card.getElementById("tier-select").select("option[selected]").first();
          title = e.text();
          if (e.attr("value").endsWith("team=1")) {
            isTeam = true;
          }
        } catch (Exception exception) {
          log.atWarn().log(card.baseUri() + " - Chart.title: " + exception.getMessage());
          title = "None";
        }
      }
    }
    List<Stage> stages = new ArrayList<>();
    {
      for (Element e : card.select("thead th:has(a)")) {
        try {
          stages.add(stageService.parse(
              Jsoup.connect("https://yoklmnracing.ru" + e.select("a").attr("href")).get()));
        } catch (Exception exception) {
          log.atWarn().log(card.baseUri() + " - Chart.Stage: " + exception.getMessage());
        }
      }
    }

    List<String> result = card.select("tbody tr td.text-center:last-child").stream()
        .map(Element::text).toList();
    List<Row> rows = new ArrayList<>();
    List<Team> teams = new ArrayList<>();
    List<Racer> racers = new ArrayList<>();
    {
      Element tableHtml;
      if (isTeam) {
        tableHtml = card.select("div#standings table:has(th:contains(Команда))").first();
      } else {
        tableHtml = card.select("div#standings table:has(th:contains(Пилот))").first();
      }
      if (tableHtml != null) {
        for (Element e : tableHtml.select("td:eq(1)")) {
          try {
            Element element = Jsoup.connect(
                "https://yoklmnracing.ru" + e.select("a").attr("href")).get();
            if (isTeam) {
              teams.add(teamService.parse(element));
            } else {
              racers.add(racerService.parse(element));
            }
          } catch (Exception exception) {
            log.atWarn().log(card.baseUri() + " - Chart.Participant: " + exception.getMessage());
          }
        }
        List<String> elementsInnerTable = tableHtml.select("td.text-center:not(td:last-child)")
            .stream().map(Element::text).toList();
        int batchSize = card.select("thead th:has(a)").size();
        rows = IntStream.range(0,
                (elementsInnerTable.size() + batchSize - 1) / batchSize).mapToObj(
                i -> elementsInnerTable.subList(i * batchSize,
                    Math.min((i + 1) * batchSize, elementsInnerTable.size())))
            .map(batch -> Row.builder().data(batch).build()).toList();
      }
    }
    Chart chart = Chart.builder()
        .title(title)
        .url(url)
        .isTeam(isTeam)
        .result(result)
        .rows(rows)
        .stages(stages)
        .racers(racers)
        .teams(teams)
        .build();
    for (Row row : rows) {
      row.setChart(chart);
    }
    for (Stage stage : stages) {
      stage.setChart(chart);
    }
    for (Team team : teams) {
      team.setChart(chart);
    }
    for (Racer racer : racers) {
      racer.setChart(chart);
    }
    log.atInfo().log("    " + title + " - Chart was successfully parsed.");
    return chart;
  }
}

