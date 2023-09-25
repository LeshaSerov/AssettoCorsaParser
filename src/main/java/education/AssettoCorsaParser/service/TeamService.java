package education.AssettoCorsaParser.service;


import education.AssettoCorsaParser.entity.participant.Team;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService implements ParsingService {

  @Override
  public Team parse(Element card) {
    String name = "";
    {
      Element teamNameElement = card.select("h1.card-title").first();
      if (teamNameElement != null) {
        name = teamNameElement.text().trim();
      }
    }
    Set<String> racers = new LinkedHashSet<>();
    {
      Element teamMembersElement = card.select("td.first + td a").first();
      if (teamMembersElement != null) {
        String teamRacers = teamMembersElement.text().trim();
        for (String racer : teamRacers.split("\n")) {
          racers.add(racer.trim());
        }
      }
    }
    Team team = Team.builder()
        .name(name)
        .racers(racers)
        .build();
    log.atInfo().log("        " + name + " - Team was successfully parsed");
    return team;
  }

}
