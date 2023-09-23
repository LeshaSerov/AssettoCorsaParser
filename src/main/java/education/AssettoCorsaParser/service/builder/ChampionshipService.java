package education.AssettoCorsaParser.service.builder;

import education.AssettoCorsaParser.entity.championship.Championship;
import education.AssettoCorsaParser.entity.championship.Chart;
import education.AssettoCorsaParser.entity.championship.Stage;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import education.AssettoCorsaParser.repository.championship.ChartRepository;
import education.AssettoCorsaParser.service.ParsingService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChampionshipService implements ParsingService {

  private final ChartRepository chartRepository;

  private final ChampionshipRepository championshipRepository;
  private final ChartService chartService;

  @Override
  public Championship parse(Element card) {

    Championship existingChampionship = null;
    int internalId = 0;
    {
      try {
        internalId = Integer.parseInt(
            card.select("link[rel=canonical]").attr("href").replaceAll("\\D", ""));
        Optional<Championship> optionalChampionship = championshipRepository.findByInternalId(
            internalId);
        if (optionalChampionship.isPresent()) {
          existingChampionship = optionalChampionship.get();
        }
      } catch (Exception exception) {
        log.atWarn().log(card.baseUri() + " - Championship.InternalId: " + exception.getMessage());
      }
    }
    String name = card.select("h1.card-title").text();
    String status = card.select("td:has(i.fab.fa-solid.fa-flag-checkered) + td").text();
    String organization = card.select("td:contains(Организатор) + td a").text();
    String simulator = card.select("td:has(i.fab.fa-solid.fa-gamepad)").text();
    LocalDate beginDate = null;
    LocalDate endDate = null;
    {
      Element elementDate = card.select("td:has(i.fab.fa-solid.fa-calendar-days) + td")
          .first();
      if (elementDate != null) {
        String[] dateParts = elementDate.text().trim().split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy",
            Locale.ENGLISH);
        beginDate = LocalDate.parse(dateParts[0], formatter);
        endDate = LocalDate.parse(dateParts[1], formatter);
      }
    }

    List<Chart> charts = new ArrayList<>();
    {
      Element selectorTables = card.getElementById("tier-select");
      String urlTable =
          "https://yoklmnracing.ru/championships/" + internalId + "?tab=standings";
      if (selectorTables == null) {
        try {
          charts.add(chartService.parse(Jsoup.connect(urlTable).get()));
        } catch (Exception exception) {
          log.atWarn().log(card.baseUri() + " - Championship.OneChart: " + exception.getMessage());
        }
      } else {
        for (Element e : selectorTables.select("option")) {
          try {
            String url = urlTable + "&" + e.attr("value");
            charts.add(chartService.parse(Jsoup.connect(url).get()));
          } catch (Exception exception) {
            log.atWarn().log(card.baseUri() + " - Championship.Chart: " + exception.getMessage());
          }
        }
      }
    }

    if (existingChampionship == null) {
      Championship championship = Championship.builder()
          .internalId(internalId)
          .name(name)
          .status(status)
          .organization(organization)
          .simulator(simulator)
          .beginDate(beginDate)
          .endDate(endDate)
          .charts(charts)
          .build();
      for (Chart chart : charts) {
        chart.setChampionship(championship);
      }
      championshipRepository.save(championship);
      log.atInfo().log(name + " - Championship was successfully saved");
      return championship;
    }
    else {
      existingChampionship.setInternalId(internalId);
      existingChampionship.setName(name);
      existingChampionship.setStatus(status);
      existingChampionship.setOrganization(organization);
      existingChampionship.setSimulator(simulator);
      existingChampionship.setBeginDate(beginDate);
      existingChampionship.setEndDate(endDate);
      chartRepository.deleteAll(existingChampionship.getCharts());
      existingChampionship.setCharts(charts);
      for (Chart chart : charts) {
        chart.setChampionship(existingChampionship);
      }
      return existingChampionship;
    }
  }

}
