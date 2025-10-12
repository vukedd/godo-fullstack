package com.app.godo.tasks;

import com.app.godo.models.Event;
import com.app.godo.models.Image;
import com.app.godo.repositories.event.EventRepository;
import com.app.godo.repositories.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecurrentEventGeneratorTask {

    private final EventRepository eventRepository;

    private static final int GENERATION_THRESHOLD_WEEKS = 1;
    private static final int WEEKS_TO_GENERATE = 1;

    private static final Logger logger = LogManager.getLogger(RecurrentEventGeneratorTask.class);

    @Scheduled(cron = "0 48 14 * * ?")
    @Transactional
    public void generateFutureRecurrentEvents() {
        logger.info("Starting recurrent event generation task...");
        List<Event> allFutureRecurrentEvents = eventRepository.findByRecurrentIsTrueAndDateAfter(LocalDate.now());

        Map<String, List<Event>> eventSeriesMap = allFutureRecurrentEvents.stream()
                .collect(Collectors.groupingBy(Event::getName));

        logger.info("Found {} active recurrent event series.", eventSeriesMap.size());

        for (var entry : eventSeriesMap.entrySet()) {
            List<Event> seriesInstances = entry.getValue();

            Event lastEvent = seriesInstances.stream()
                    .max(Comparator.comparing(Event::getDate))
                    .orElse(null);

            if (lastEvent == null) continue;

            LocalDate generationThreshold = LocalDate.now().plusWeeks(GENERATION_THRESHOLD_WEEKS);
            if (lastEvent.getDate().isBefore(generationThreshold)) {
                logger.info("Series '{}' at venue {} needs new events. Last event is on {}.",
                        lastEvent.getName(), lastEvent.getVenue().getId(), lastEvent.getDate());

                generateNextBatch(lastEvent);
            }
        }

        logger.info("Finished recurrent event generation job.");
    }


    private void generateNextBatch(Event templateEvent) {
        List<Event> newInstances = new ArrayList<>();
        LocalDate nextDate = templateEvent.getDate().plusWeeks(1);

        for (int i = 0; i < WEEKS_TO_GENERATE; i++) {
            Event newInstance = Event.builder()
                    .name(templateEvent.getName())
                    .description(templateEvent.getDescription())
                    .eventType(templateEvent.getEventType())
                    .date(nextDate)
                    .address(templateEvent.getAddress())
                    .price(templateEvent.getPrice())
                    .recurrent(true)
                    .venue(templateEvent.getVenue())
                    .build();

            newInstance.setImage(
                    Image.builder()
                            .eventImageOf(newInstance)
                            .path(templateEvent.getImage().getPath()).build()
            );

            newInstances.add(newInstance);
            nextDate = nextDate.plusWeeks(1);
        }

        eventRepository.saveAll(newInstances);
        logger.info("Generated {} new instances for series '{}'.", newInstances.size(), templateEvent.getName());
    }
}
