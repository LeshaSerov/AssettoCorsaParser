package education.AssettoCorsaParser.service.builder;

import education.AssettoCorsaParser.entity.championship.Chart;
import education.AssettoCorsaParser.entity.championship.Row;
import education.AssettoCorsaParser.entity.championship.Stage;
import education.AssettoCorsaParser.entity.participant.Racer;
import education.AssettoCorsaParser.entity.participant.Team;
import education.AssettoCorsaParser.repository.championship.ChartRepository;
import education.AssettoCorsaParser.repository.championship.RowRepository;
import education.AssettoCorsaParser.repository.championship.StageRepository;
import education.AssettoCorsaParser.repository.participant.RacerRepository;
import education.AssettoCorsaParser.repository.participant.TeamRepository;
import education.AssettoCorsaParser.service.ParsingService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

  private final TeamRepository teamRepository;

  private final RacerRepository racerRepository;

  private final StageRepository stageRepository;

  private final RowRepository rowRepository;

  private final ChartRepository chartRepository;
  private final StageService stageService;
  private final TeamService teamService;
  private final RacerService racerService;

  @Override
  public Chart parse(Element card) {

    Chart existingChart = null;

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
      Optional<Chart> optionalChart = chartRepository.findByUrl(url);
      if (optionalChart.isPresent()) {
        existingChart = optionalChart.get();
      }
    }

    Set<Stage> stages = new LinkedHashSet<>();
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
    Set<Team> teams = new LinkedHashSet<>();
    Set<Racer> racers = new LinkedHashSet<>();
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

    if (existingChart == null) {
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
      setOwnerChart(chart, stages, rows, teams, racers);
      log.atInfo().log(title + " - Chart was successfully parsed");
      chartRepository.save(chart);
      log.atInfo().log(title + " - Chart was successfully saved");
      return chart;
    } else {
      existingChart.setTitle(title);
      existingChart.setUrl(url);
      existingChart.setIsTeam(isTeam);
      existingChart.setResult(result);
      rowRepository.deleteAll(existingChart.getRows());
      existingChart.setRows(rows);
      stageRepository.deleteAll(existingChart.getStages());
      existingChart.setStages(stages);
      racerRepository.deleteAll(existingChart.getRacers());
      existingChart.setRacers(racers);
      teamRepository.deleteAll(existingChart.getTeams());
      existingChart.setTeams(teams);
      setOwnerChart(existingChart, stages, rows, teams, racers);
      return existingChart;
    }
  }

  private void setOwnerChart(Chart existingChart, Set<Stage> stages, List<Row> rows,
      Set<Team> teams, Set<Racer> racers) {
    for (Row row : rows) {
      row.setChart(existingChart);
    }
    for (Stage stage : stages) {
      stage.getCharts().add(existingChart);
    }
    for (Team team : teams) {
      team.getCharts().add(existingChart);
    }
    for (Racer racer : racers) {
      racer.getCharts().add(existingChart);
    }
  }

}

