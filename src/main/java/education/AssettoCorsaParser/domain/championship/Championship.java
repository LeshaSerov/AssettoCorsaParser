package education.AssettoCorsaParser.domain.championship;

import education.AssettoCorsaParser.domain.Parsing;
import education.AssettoCorsaParser.domain.participant.Racer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;


@Entity
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Table
@Slf4j
public class Championship implements Parsing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer internalId;
    private String name;
    private String status;
    private String simulator;
    private String organization;
    private LocalDate beginDate;
    private LocalDate endDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "championship", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TableResult> tableResults = new ArrayList<>();

    @Override
    public Championship parseAndPopulate(Element card) {
        try {
            Element elementDate = card.select("td:has(i.fab.fa-solid.fa-calendar-days) + td")
                .first();
            if (elementDate != null) {
                String[] dateParts = elementDate.text().trim().split(" - ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy",
                    Locale.ENGLISH);
                beginDate = LocalDate.parse(dateParts[0], formatter);
                endDate = LocalDate.parse(dateParts[1], formatter);
            }

            internalId = Integer.parseInt(
                card.select("link[rel=canonical]").attr("href").replaceAll("\\D", ""));
            name = card.select("h1.card-title").text();
            status = card.select("td:has(i.fab.fa-solid.fa-flag-checkered) + td").text();
            organization = card.select("td:contains(Организатор) + td a").text();
            simulator = card.select("td:has(i.fab.fa-solid.fa-gamepad)").text();

            Element tables = card.getElementById("tier-select");
            String urlTable =
                "https://yoklmnracing.ru/championships/" + internalId + "?tab=standings";
            if (tables == null) {
                TableResult tableResult = new TableResult("Личный", urlTable);
                tableResult.parseAndPopulate(card);
                tableResult.setChampionship(this);
                tableResults.add(tableResult);
            } else {
                for (Element e : tables.select("option")) {
                    TableResult tableResult = new TableResult(e.text(),
                        urlTable + "&" + e.attr("value"));
                    if (e.attr("value").endsWith("team=1")) {
                        tableResult.setIsTeamResult(true);
                    }
                    tableResult.parseAndPopulate(Jsoup.connect(tableResult.getUrl()).get());
//                    tableResult.setChampionship(this);
                    tableResults.add(tableResult);
                }
            }

            for (TableResult tableResult : tableResults) {
                tableResult.setChampionship(this);
            }

            log.atInfo().log(name + " - Championship was successfully parsed");
        } catch (Exception e) {
            log.atDebug().log(card.baseUri() + e.getMessage());
        }

        return this;
    }
}
