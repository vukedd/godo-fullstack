package com.app.godo.repositories.venue;

import com.app.godo.enums.VenueType;
import com.app.godo.models.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("SELECT v FROM Venue v WHERE v.name LIKE CONCAT('%', :venueName, '%') OR v.address LIKE CONCAT('%', :venueAddress, '%')")
    Page<Venue> filterVenues(@Param("venueName") String name, @Param("venueAddress") String address, Pageable pageable);

    @Query("SELECT v FROM Venue v WHERE (v.name LIKE CONCAT('%', :venueName, '%') OR v.address LIKE CONCAT('%', :venueAddress, '%')) AND v.type = :venueType")
    Page<Venue> filterVenuesWithType(@Param("venueName") String name, @Param("venueAddress") String address, @Param("venueType") VenueType venueType, Pageable pageable);

    Venue findVenueByName(String name);
}
