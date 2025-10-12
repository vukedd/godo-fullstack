package com.app.godo.services.event;

import com.app.godo.dtos.event.CreateEventRequestDto;
import com.app.godo.dtos.event.EventDetailsDto;
import com.app.godo.dtos.event.UpcomingEventDto;
import com.app.godo.dtos.venue.CreateVenueRequestDto;
import com.app.godo.enums.EventType;
import com.app.godo.exceptions.general.ConflictException;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.ParseException;
import com.app.godo.models.Event;
import com.app.godo.models.Image;
import com.app.godo.models.Venue;
import com.app.godo.repositories.event.EventRepository;
import com.app.godo.repositories.image.ImageRepository;
import com.app.godo.repositories.venue.VenueRepository;
import com.app.godo.services.files.FileStorageService;
import com.app.godo.specifications.EventSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final FileStorageService fileStorageService;

    private final ObjectMapper objectMapper;

    private static final int WEEKS_TO_GENERATE_IN_ADVANCE = 1;
    private static final Logger logger = LogManager.getLogger(EventService.class);

    public Page<EventDetailsDto> filterEvents(String filter, double priceFrom, double priceTo, int eventType, LocalDate eventDate, Pageable eventPage) {
        Specification<Event> spec = EventSpecification.empty();

        if (filter != null && !filter.isEmpty()) {
            spec = spec.and(EventSpecification.hasText(filter));
        }

        if (priceFrom != -1) {
            spec = spec.and(EventSpecification.priceIsGreaterThanOrEqual(priceFrom));
        }

        if (priceTo != -1) {
            spec = spec.and(EventSpecification.priceIsLessThanOrEqual(priceTo));
        }

        if (eventType != -1) {
            spec = spec.and(EventSpecification.hasEventType(EventType.values()[eventType]));
        }

        if (eventDate != null) {
            spec = spec.and(EventSpecification.dateIsEqualTo(eventDate));
        }

        return eventRepository.findAll(spec, eventPage).map(EventDetailsDto::fromEntity);
    }

    public List<EventDetailsDto> findEventsHappeningToday() {
        return eventRepository.findAllByDateEquals(LocalDate.now()).stream().map(EventDetailsDto::fromEntity).toList();
    }


    @Transactional
    public CreateEventRequestDto createEvent(long venueId, CreateEventRequestDto dto, MultipartFile image) {
        Venue venue;

        try {
            venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new NotFoundException("Venue not found"));

            logger.info("Venue with the Id: {} has been found!", venueId);
        } catch (NotFoundException ex) {
            logger.warn("Event creation failed. Venue with the Id: {} couldn't be found..", venueId);
            throw new NotFoundException(ex.getMessage());
        }

        String path = "http://localhost:8080/uploads/" + fileStorageService.storeFile(image);

        if (dto.isRecurrent()) {
            if (!eventRepository.findByRecurrentIsTrueAndName(dto.getName()).isEmpty()) {
                logger.warn("Event creation failed. A recurrent event with the name {} already exists...", dto.getName());
                throw new ConflictException("a recurrent event with the same name already exists");
            }

            logger.info("Event is recurrent, a series of events will be created!");

            LocalDate currentDate = dto.getDate();
            List<Event> eventInstances = new ArrayList<>();

            for (int i = 0; i < WEEKS_TO_GENERATE_IN_ADVANCE; i++) {
                Event instance = Event.builder()
                        .name(dto.getName())
                        .description(dto.getDescription())
                        .eventType(dto.getType())
                        .date(currentDate)
                        .address(dto.getAddress())
                        .price(dto.getPrice())
                        .recurrent(true)
                        .venue(venue)
                        .build();

                instance.setImage(
                        Image.builder()
                                .eventImageOf(instance)
                                .path(path).build()
                );

                eventInstances.add(instance);
                currentDate = currentDate.plusWeeks(1);
            }

            eventRepository.saveAll(eventInstances);
            logger.info("Recurrent event series have been successfully added!");

        } else {
            Event event = Event.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .eventType(dto.getType())
                    .date(dto.getDate())
                    .address(dto.getAddress())
                    .price(dto.getPrice())
                    .recurrent(false)
                    .venue(venue)
                    .build();

            event.setImage(
                    Image.builder()
                            .eventImageOf(event)
                            .path(path).build()
            );

            event = eventRepository.save(event);
            logger.info("Event has been successfully added, ID: {}", event.getId());
        }
        return dto;
    }

    public List<UpcomingEventDto> findAllUpcomingEventsByVenueId(long venueId) {
        Venue venue;

        try {
            venue = venueRepository.findById(venueId)
                    .orElseThrow(() -> new NotFoundException("Venue not found"));
        } catch (NotFoundException ex) {
            logger.warn("Couldn't fetch upcoming event, Venue with the Id: {} couldn't be found..", venueId);
            throw new NotFoundException(ex.getMessage());
        }

        logger.info("Finding upcoming events for venue: {}", venue.getName());

        List<Event> upcomingEvents = eventRepository.findByVenueAndDateAfter(venue, LocalDate.now());

        logger.info("Found {} upcoming events for this venue", upcomingEvents.size());

        return upcomingEvents.stream().map(UpcomingEventDto::fromEntity).toList();
    }

    public CreateEventRequestDto convertToCreateEventRequest(String eventJson) {
        CreateEventRequestDto event;
        try {
            event = objectMapper.readValue(eventJson, CreateEventRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("An expected error has occurred please try again in a moment!");
        }

        return event;
    }

    public EventDetailsDto findEventById(long eventId) {
        Event event;

        logger.info("Looking for event with the Id: {}", eventId);

        try {
            event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("the event you were looking for couldn't be found!"));
        } catch (NotFoundException ex) {
            logger.warn("The event you were looking for couldn't be found!");
            throw new NotFoundException(ex.getMessage());
        }

        logger.info("The event you were looking has been found! Event name: {}", event.getName());


        return EventDetailsDto.fromEntity(event);
    }
}
