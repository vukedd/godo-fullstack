package com.app.godo.repositories.venue;

import com.app.godo.enums.VenueType;
import com.app.godo.models.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    @Query("""
    SELECT v FROM Venue v
    WHERE LOWER(v.name) LIKE CONCAT('%', LOWER(:venueName), '%')
       OR LOWER(v.address) LIKE CONCAT('%', LOWER(:venueAddress), '%')
    """)
    Page<Venue> filterVenues(@Param("venueName") String name,
                             @Param("venueAddress") String address,
                             Pageable pageable);

    @Query("""
    SELECT v FROM Venue v
    WHERE (LOWER(v.name) LIKE CONCAT('%', LOWER(:venueName), '%')
        OR LOWER(v.address) LIKE CONCAT('%', LOWER(:venueAddress), '%'))
      AND v.type = :venueType
    """)
    Page<Venue> filterVenuesWithType(@Param("venueName") String name,
                                     @Param("venueAddress") String address,
                                     @Param("venueType") VenueType venueType,
                                     Pageable pageable);

    Venue findVenueByName(String name);
    Optional<Venue> findVenueById(long id);

    @Query("""
    SELECT v
        FROM Venue v
            LEFT JOIN v.reviews r
                LEFT JOIN r.rating rat
                    GROUP BY v.id
                        ORDER BY COALESCE(AVG((rat.performance + rat.ambient + rat.venue + rat.overallImpression) / 4.0), 0) DESC
    """)
    List<Venue> findAllOrderByCalculatedRatingDesc();
}
