package education.AssettoCorsaParser.service.builder;

import education.AssettoCorsaParser.entity.participant.Racer;
import education.AssettoCorsaParser.repository.participant.RacerRepository;
import education.AssettoCorsaParser.service.ParsingService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RacerService implements ParsingService {

  private final RacerRepository racerRepository;

  @Override
  public Racer parse(Element card) {

    Racer existingRacer = null;
    String url = card.baseUri();
    {
      Element profileUrlElement = card.select("h1.card-title a").first();
      if (profileUrlElement != null) {
        url = profileUrlElement.attr("href");
        Optional<Racer> optionalRacer = racerRepository.findByUrl(url);
        if (optionalRacer.isPresent()) {
          existingRacer = optionalRacer.get();
        }
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

    if (existingRacer == null) {
      Racer racer = Racer.builder()
          .url(url)
          .name(name)
          .city(city)
          .country(country)
          .build();
      log.atInfo().log(name + " - Racer was successfully parsed");
      racerRepository.save(racer);
      log.atInfo().log(name + " - Racer was successfully saved");
      return racer;
    } else {
      existingRacer.setUrl(url);
      existingRacer.setName(name);
      existingRacer.setCity(city);
      existingRacer.setCountry(country);
      return existingRacer;
    }
  }
}

