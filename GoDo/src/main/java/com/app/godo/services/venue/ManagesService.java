package com.app.godo.services.venue;

import com.app.godo.dtos.manages.ManagesOverviewDto;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.models.Manages;
import com.app.godo.models.User;
import com.app.godo.models.Venue;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.repositories.venue.ManagesRepository;
import com.app.godo.repositories.venue.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagesService {
    private final ManagesRepository managesRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;

    public List<ManagesOverviewDto> getManagementByVenueId(long venueId) {
        List<Manages> manages;

        Venue venue = venueRepository.findVenueById(venueId)
                .orElseThrow(() -> new NotFoundException("The venue you were looking for can't be found"));

        manages = managesRepository.findManagesByVenue(venue);

        return manages.stream().map(ManagesOverviewDto::fromEntity).toList();
    }

    public List<ManagesOverviewDto> getManagementByUsername(String username) {
        List<Manages> manages;

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("The user you were looking for can't be found"));

        manages = managesRepository.findManagesByManager(manager);

        return manages.stream().map(ManagesOverviewDto::fromEntity).toList();
    }
}
