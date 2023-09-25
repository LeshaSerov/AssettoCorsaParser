package education.AssettoCorsaParser.controllers;

import education.AssettoCorsaParser.entity.championship.Championship;
import education.AssettoCorsaParser.repository.championship.ChampionshipRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/championships")
public class ChampionshipController {

  private final ChampionshipRepository championshipRepository;

  @Autowired
  public ChampionshipController(ChampionshipRepository championshipRepository) {
    this.championshipRepository = championshipRepository;
  }

  @GetMapping("/")
    public List<Championship> getAll(){
    return championshipRepository.findAll();
    }

  @GetMapping("/{Id}")
  public Championship getChampionshipById(@PathVariable Long Id) {
    return championshipRepository.findById(Id).orElse(Championship.builder().id(0L).internalId(0).build());
  }

}
