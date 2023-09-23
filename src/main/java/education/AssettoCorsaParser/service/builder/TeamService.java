package education.AssettoCorsaParser.service.builder;


import education.AssettoCorsaParser.entity.participant.Racer;
import education.AssettoCorsaParser.entity.participant.Team;
import education.AssettoCorsaParser.repository.participant.RacerRepository;
import education.AssettoCorsaParser.repository.participant.TeamRepository;
import education.AssettoCorsaParser.service.ParsingService;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService implements ParsingService {

  private final RacerRepository racerRepository;

  private final TeamRepository teamRepository;

  @Override
  public Team parse(Element card) {

    Team existingTeam = null;
    String name = "";
    {
      Element teamNameElement = card.select("h1.card-title").first();
      if (teamNameElement != null) {
        name = teamNameElement.text().trim();
        Optional<Team> optionalTeam = teamRepository.findByName(name);
        if (optionalTeam.isPresent()) {
          existingTeam = optionalTeam.get();
        }
      }
    }

    Set<Racer> racers = new LinkedHashSet<>();
    {
      Element teamMembersElement = card.select("td.first + td a").first();
      if (teamMembersElement != null) {
        String teamRacers = teamMembersElement.text().trim();
        for (String racer : teamRacers.split("\n")) {
          Optional<Racer> optionalRacer = racerRepository.findByName(racer.trim());
          if (optionalRacer.isPresent()) {
            racers.add(optionalRacer.get());
          } else {
            racers.add(Racer.builder().name(racer.trim()).build());
          }
        }
      }
    }

    if (existingTeam == null) {
      Team team = Team.builder()
          .name(name)
          .racers(racers)
          .build();
      for (Racer racer : racers) {
        racer.setTeam(team);
      }
      log.atInfo().log(name + " - Team was successfully parsed");
      teamRepository.save(team);
      log.atInfo().log(name + " - Team was successfully saved");
      return team;
    } else {
      existingTeam.setName(name);
      racerRepository.deleteAll(existingTeam.getRacers());
      existingTeam.setRacers(racers);
      for (Racer racer : racers) {
        racer.setTeam(existingTeam);
      }
      return existingTeam;
    }
  }

}
