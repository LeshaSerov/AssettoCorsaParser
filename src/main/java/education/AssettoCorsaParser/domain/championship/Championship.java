package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.Pilot;
import education.AssettoCorsaParser.domain.championship.result.AbstractTableResult;
import education.AssettoCorsaParser.domain.championship.result.PilotTableResult;
import jakarta.persistence.*;
import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@ToString
public class Championship implements Parsing {

    /*
     * IMPORTANT:
     * Set toString, equals, and hashCode as described in these
     * documents:
     * - https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
     * - https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     * - https://vladmihalcea.com/hibernate-facts-equals-and-hashcode/
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer internalId;
    private String name;
    private String status;
    private String simulator;
    private String organization;
    private LocalDate beginDate;
    private LocalDate endDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "championship_pilot",
            joinColumns = @JoinColumn(name = "championship_id"),
            inverseJoinColumns = @JoinColumn(name = "pilot_id")
    )
    private List<Pilot> pilots = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "championship", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stage> stages = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AbstractTableResult tableResult;

    @Override
    public Championship parseAndPopulate(Element card) {
        String[] dateParts = card.select("td:has(i.fab.fa-solid.fa-calendar-days) + td").first().text().trim().split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);

        this.internalId = Integer.parseInt(card.select("link[rel=canonical]").attr("href").replaceAll("\\D", ""));
        this.name = card.select("h1.card-title").text();
        this.status = card.select("td:has(i.fab.fa-solid.fa-flag-checkered) + td").text();
        this.organization = card.select("td:has(i.fab.fa-solid.fa-at) a").text();
        this.simulator = card.select("td:has(i.fab.fa-solid.fa-gamepad)").text();
        this.beginDate = LocalDate.parse(dateParts[0], formatter);
        this.endDate = LocalDate.parse(dateParts[1], formatter);

        Document document = Jsoup.parse("https://yoklmnracing.ru/championships/" + internalId + "?tab=standings");


//        for (Element e : Objects.requireNonNull(card.select("h3:contains(Участники) + div.table-responsive table tbody tr"))) {
//            Pilot pilot = new Pilot().parseAndPopulate(e);
//            pilot.getChampionships().add(this);
//            pilots.add(pilot);
//        }
//
//        for (Element e : card.select("h3:contains(Этапы) + div.tracks-list table tbody tr")) {
//            Stage stage = new Stage().parseAndPopulate(e);
//            stage.setChampionship(this);
//            stages.add(stage);
//        }

        return this;
    }


//                .internalId(Integer.parseInt(Objects.requireNonNull(card.select("h1.card-title a").first())
//                        .attr("href").replaceAll("\\D", ""))
//                )
//                .name(card.select("h1.card-title a").text())
////                .status(card.select("span.badge.text-bg-success").text())
//                .status(card.select("td:has(i.fab.fa-solid.fa-flag-checkered) + td").text())
//                .simulator(Objects.requireNonNull(card.select("td:has(i.fab.fa-solid.fa-gamepad) + td").first()).text())
////                .numberStages(Integer.parseInt(card.select("td:has(i.fab.fa-solid.fa-flag-checkered)").text().split(" ")[0]))
////                .organization(card.select("td:has(i.fab.fa-solid.fa-at)").text())
//                .organization(card.select("td:has(i.fab.fa-solid.fa-at) + td").text())
//                .beginDate(LocalDate.parse(dateParts[0], formatter))
//                .endDate(LocalDate.parse(dateParts[1], formatter))
//                .build();
//

//    private static Championship parseChampionship(Document document) {
//        Championship championship = new Championship();
//        // Extract championship data from HTML and populate the 'championship' object
//        championship.setName(document.select("h1.card-title").text());
//        championship.setStatus(document.select("td:contains(Status) + td").text());
//        championship.setOrganization(document.select("td:contains(Organizer) + td a").text());
//        championship.setSimulator(document.select("td:contains(Simulator) + td").text());
//        championship.setDates(document.select("td:contains(Dates) + td").text());
//
//        // Additionally, you can extract event data
//        Elements eventElements = document.select("td:contains(Events) + td a");
//        List<String> events = new ArrayList<>();
//        for (Element eventElement : eventElements) {
//            events.add(eventElement.text());
//        }
//        championship.setEvents(events);
//
//        return championship;
//    }
//
//    private static List<Stage> parseChampionshipStages(Document document) {
//        List<Stage> stages = new ArrayList<>();
//        // Extract championship stages data from HTML and populate the 'stages' list
//        Elements stageElements = document.select("div.tracks-list table tr");
//        for (Element stageElement : stageElements) {
//            Stage stage = new Stage();
//            stage.setStageNumber(Integer.parseInt(stageElement.select("td:first-child").text()));
//            stage.setTrackName(stageElement.select("td:nth-child(2)").text());
//            stage.setLeague(stageElement.select("td:nth-child(3) a").text());
//            stage.setSchedule(stageElement.select("td:nth-child(4)").text());
//            stage.setDate(stageElement.select("td:nth-child(5)").text());
//            stages.add(stage);
//        }
//        return stages;
//    }
//
//    private static List<Result> parseChampionshipResults(Document document) {
//        List<Result> results = new ArrayList<>();
//        // Extract championship results data from HTML and populate the 'results' list
//        Elements resultElements = document.select("div#standings table tbody tr");
//        for (Element resultElement : resultElements) {
//            Result result = new Result();
//            result.setPosition(Integer.parseInt(resultElement.select("td:first-child").text()));
//            result.setDriverName(resultElement.select("td:nth-child(2) a").text());
//            result.setTeam(resultElement.select("td:nth-child(4) a").text());
//            result.setCategory(resultElement.select("td:nth-child(5)").text());
//            result.setCar(resultElement.select("td:nth-child(6)").text());
//            result.setTotalPoints(Integer.parseInt(resultElement.select("td:last-child").text()));
//            results.add(result);
//        }
//        return results;
//    }


}

// Вывод информации в консоль
//System.out.println("Название чемпионата: " + championshipName);
//System.out.println("Статус чемпионата: " + championshipStatus);
//System.out.println("Игра: " + game);
//System.out.println("Количество этапов: " + stages);
//System.out.println("Организация: " + organization);
//System.out.println("Даты чемпионата: " + date);
//System.out.println("--------------------------------------");
//
//}