package ru.javaops.masterjava.upload;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class CityProcessor {
    private static final int NUMBER_THREADS = 4;

    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static CityDao cityDao = DBIProvider.getDao(CityDao.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    @AllArgsConstructor
    public static class FailedCities {
        public String citiesOrRange;
        public String reason;

        @Override
        public String toString() {
            return citiesOrRange + " : " + reason;
        }
    }

    /*
     * return failed users chunks
     */
    public List<FailedCities> process(final InputStream is, int chunkSize) throws XMLStreamException, JAXBException {
        log.info("Start processing with chunkSize=" + chunkSize);

        Map<String, Future<List<String>>> chunkFutures = new LinkedHashMap<>();  // ordered map (emailRange -> chunk future)

        int id = cityDao.getSeqAndSkip(chunkSize);
        List<City> chunk = new ArrayList<>(chunkSize);
        val processor = new StaxStreamProcessor(is);
        val unmarshaller = jaxbParser.createUnmarshaller();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "City")) {
            ru.javaops.masterjava.xml.schema.CityType xmlCity = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.CityType.class);
            final City city = new City(id++, xmlCity.getValue());
            chunk.add(city);
            if (chunk.size() == chunkSize) {
                addChunkFutures(chunkFutures, chunk);
                chunk = new ArrayList<>(chunkSize);
                id = cityDao.getSeqAndSkip(chunkSize);
            }
        }

        if (!chunk.isEmpty()) {
            addChunkFutures(chunkFutures, chunk);
        }

        List<FailedCities> failed = new ArrayList<>();
        List<String> allAlreadyPresents = new ArrayList<>();
        chunkFutures.forEach((emailRange, future) -> {
            try {
                List<String> alreadyPresentsInChunk = future.get();
                log.info("{} successfully executed with already presents: {}", emailRange, alreadyPresentsInChunk);
                allAlreadyPresents.addAll(alreadyPresentsInChunk);
            } catch (InterruptedException | ExecutionException e) {
                log.error(emailRange + " failed", e);
                failed.add(new FailedCities(emailRange, e.toString()));
            }
        });
        if (!allAlreadyPresents.isEmpty()) {
            failed.add(new FailedCities(allAlreadyPresents.toString(), "already presents"));
        }
        return failed;
    }

    private void addChunkFutures(Map<String, Future<List<String>>> chunkFutures, List<City> chunk) {
        String cityRange = String.format("[%s-%s]", chunk.get(0).getName(), chunk.get(chunk.size() - 1).getName());
        Future<List<String>> future = executorService.submit(() -> cityDao.insertAndGetConflictCities(chunk));
        chunkFutures.put(cityRange, future);
        log.info("Submit chunk: " + cityRange);
    }
}
