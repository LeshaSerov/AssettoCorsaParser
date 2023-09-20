/*
package education.AssettoCorsaParser.ignored;

import education.AssettoCorsaParser.domain.championship.Championship;
import education.AssettoCorsaParser.domain.championship.TableResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TableResultCreator {
    private final ExecutorService executor = Executors.newFixedThreadPool(10); // Максимальное количество одновременных запросов

    public List<TableResult> createTableResults(Elements options, String urlTable, Championship championship) throws InterruptedException {
        AtomicInteger count = new AtomicInteger(options.size());
        AtomicReference<List<TableResult>> tableResultsRef = new AtomicReference<>(new ArrayList<>());

        CountDownLatch latch = new CountDownLatch(options.size());

        for (Element e : options) {
            executor.submit(() -> {
                try {
                    TableResult tableResult = createTableResult(e, urlTable, championship);
                    synchronized (tableResultsRef) {
                        tableResultsRef.get().add(tableResult);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace(); // Обработка ошибок
                } finally {
                    latch.countDown();
                    if (count.decrementAndGet() == 0) {
                        executor.shutdown();
                    }
                }
            });
        }

        latch.await(); // Ждем завершения всех задач

        return tableResultsRef.get();
    }

    private TableResult createTableResult(Element e, String urlTable, Championship championship) throws IOException {
        TableResult tableResult = new TableResult(e.text(), urlTable + "&" + e.attr("value"));
        if (e.attr("value").endsWith("team=1")) {
            tableResult.setIsTeamResult(true);
        }
        tableResult.parseAndPopulate(Jsoup.connect(tableResult.getUrl()).get());
        tableResult.setChampionship(championship);
        return tableResult;
    }
}

*/
