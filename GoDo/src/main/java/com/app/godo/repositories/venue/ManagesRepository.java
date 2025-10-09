package com.app.godo.repositories.venue;

import com.app.godo.models.Manages;
import com.app.godo.models.User;
import com.app.godo.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ManagesRepository extends JpaRepository<Manages, Long> {
    List<Manages> findManagesByVenue(Venue venue);
    List<Manages> findManagesByManager(User user);

    @Query("SELECT m.manager.id FROM Manages m WHERE m.venue.id = :venueId AND m.endDate IS NULL")
    Set<Long> findManagerIdsByVenueId(@Param("venueId") Long venueId);

    @Modifying
    @Query("UPDATE Manages m SET m.endDate = :endDate WHERE m.venue.id = :venueId AND m.manager.id IN :managerIds AND m.endDate IS NULL")
    void revokeManagement(@Param("venueId") Long venueId, @Param("managerIds") Set<Long> managerIds, @Param("endDate") LocalDate endDate);
}
