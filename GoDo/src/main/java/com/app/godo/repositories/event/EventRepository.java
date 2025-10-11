package com.app.godo.repositories.event;

import com.app.godo.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByRecurrentIsTrueAndDateAfter(LocalDate now);

    @Query("SELECT e FROM Event e WHERE LOWER(e.name) = LOWER(:name) AND e.recurrent IS true")
    List<Event> findByRecurrentIsTrueAndName(@Param("name") String name);

}
