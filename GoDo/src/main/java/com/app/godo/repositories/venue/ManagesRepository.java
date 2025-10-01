package com.app.godo.repositories.venue;

import com.app.godo.models.Manages;
import com.app.godo.models.User;
import com.app.godo.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagesRepository extends JpaRepository<Manages, Long> {
    List<Manages> findManagesByVenue(Venue venue);
    List<Manages> findManagesByManager(User user);

}
