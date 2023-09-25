package education.AssettoCorsaParser.service;

import education.AssettoCorsaParser.entity.participant.Racer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RacerService implements ParsingService {

  @Override
  public Racer parse(Element card) {
    String url = card.baseUri();
    {
      Element profileUrlElement = card.select("h1.card-title a").first();
      if (profileUrlElement != null) {
        url = profileUrlElement.attr("href");
      }
    }
    String name = "";
    {
      Element userNameElement = card.select("h1.card-title").first();
      if (userNameElement != null) {
        name = userNameElement.text().trim();
      }
    }
    String city = "";
    {
      Element cityElement = card.select("tr:has(td:contains(Город)) td:eq(1)").first();
      if (cityElement != null) {
        city = cityElement.text().trim();
      }
    }
    String country = "";
    {
      Element countryElement = card.select("tr:has(td:contains(Страна)) td:eq(1)").first();
      if (countryElement != null) {
        country = countryElement.text().trim();
      }
    }
    Racer racer = Racer.builder()
        .url(url)
        .name(name)
        .city(city)
        .country(country)
        .build();
    log.atInfo().log("        " + name + " - Racer was successfully parsed");
    return racer;
  }
}

