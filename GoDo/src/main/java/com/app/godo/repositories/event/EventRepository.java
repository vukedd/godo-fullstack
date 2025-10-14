package com.app.godo.repositories.event;

import com.app.godo.models.Event;
import com.app.godo.models.Venue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findByRecurrentIsTrueAndDateAfter(LocalDate now);

    @Query("SELECT e FROM Event e WHERE LOWER(e.name) = LOWER(:name) AND e.recurrent IS true")
    List<Event> findByRecurrentIsTrueAndName(@Param("name") String name);

    List<Event> findByVenue(Venue venue);

    List<Event> findByVenueAndDateAfter(Venue venue, LocalDate dateAfter);

    Page<Event> findAll(Specification<Event> spec, Pageable eventPage);

    List<Event> findAllByDateEquals(LocalDate now);

    List<Event> findByRecurrentIsTrueAndDateBeforeAndVenue(LocalDate date, Venue venue);

    @Query("SELECT COUNT(*) FROM Event e0 WHERE e0.name LIKE :eventName AND e0.recurrent IS TRUE AND e0.date < (SELECT e1.date FROM Event e1 WHERE e1.id = :eventId AND e1.recurrent IS TRUE)")
    int findEventNumber(@Param("eventId") long eventId, @Param("eventName") String eventName);
}
