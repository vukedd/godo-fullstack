package com.app.godo.services.venue;

import com.app.godo.dtos.manages.ManagementExistsDto;
import com.app.godo.dtos.manages.ManagesOverviewDto;
import com.app.godo.dtos.user.UserManagerOptionDto;
import com.app.godo.exceptions.general.BadRequestException;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.UnauthorizedException;
import com.app.godo.models.Manages;
import com.app.godo.models.User;
import com.app.godo.models.Venue;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.repositories.venue.ManagesRepository;
import com.app.godo.repositories.venue.VenueRepository;
import com.app.godo.services.auth.JwtService;
import com.app.godo.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagesService {
    private final ManagesRepository managesRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final Utils utils;

    public List<ManagesOverviewDto> getManagementByVenueId(long venueId) {
        List<Manages> manages;

        Venue venue = venueRepository.findVenueById(venueId)
                .orElseThrow(() -> new NotFoundException("The venue you were looking for can't be found"));

        manages = managesRepository.findManagesByVenue(venue);

        return manages.stream()
                .filter(manages1 -> manages1.getEndDate() == null)
                .map(ManagesOverviewDto::fromEntity).toList();
    }

    public List<UserManagerOptionDto> getManagersByVenueId(long venueId, String token) {

        if (utils.extractRole(token).equals("MEMBER")) {
            throw new UnauthorizedException("you are not allowed to perform this operation");
        }

        List<Manages> manages;

        Venue venue = venueRepository.findVenueById(venueId)
                .orElseThrow(() -> new NotFoundException("The venue you were looking for can't be found"));

        manages = managesRepository.findManagesByVenue(venue);

        return manages.stream()
                .filter(manages1 -> manages1.getEndDate() == null)
                .map(Manages::getManager)
                .map(this::convertToManagerDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateManagersByVenueId(long venueId, List<UserManagerOptionDto> managerOptions, String token) {
        if (utils.extractRole(token).equals("MEMBER")) {
            throw new UnauthorizedException("you are not allowed to perform this operation");
        }

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new NotFoundException("Venue not found with id: " + venueId));

        // 1, 2, 3
        Set<Long> currentManagerIds = managesRepository.findManagerIdsByVenueId(venueId);

        // 2, 3, 4
        Set<Long> updatedManagersIdsSet = managerOptions.stream().map(UserManagerOptionDto::getId).collect(Collectors.toSet());

        // 4
        Set<Long> managementsToStart = new java.util.HashSet<>(updatedManagersIdsSet);
        managementsToStart.removeAll(currentManagerIds);

        // 1
        Set<Long> managementsToEnd = new java.util.HashSet<>(currentManagerIds);
        managementsToEnd.removeAll(updatedManagersIdsSet);

        if (!managementsToEnd.isEmpty()) {
            managesRepository.revokeManagement(venueId, managementsToEnd, LocalDate.now());
        }

        if (!managementsToStart.isEmpty()) {
            List<User> usersToAdd = userRepository.findAllById(managementsToStart);

            if (usersToAdd.size() != managementsToStart.size()) {
                throw new BadRequestException("One or more user IDs were invalid or do not exist.");
            }

            List<Manages> newManagesRelations = usersToAdd.stream()
                    .map(user -> Manages
                            .builder()
                            .manager(user)
                            .venue(venue)
                            .startDate(LocalDate.now())
                            .build()
                    ).collect(Collectors.toList());

            managesRepository.saveAll(newManagesRelations);
        }
    }

    public List<ManagesOverviewDto> getManagementByUsername(String username) {
        List<Manages> manages;

        User manager = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("The user you were looking for can't be found"));

        manages = managesRepository.findManagesByManager(manager);

        return manages.stream()
                .filter(manages1 -> manages1.getEndDate() == null)
                .map(ManagesOverviewDto::fromEntity).toList();
    }

    public ManagementExistsDto doesManagementExist(String username, Long venueId, String token) {
        if (!utils.extractSubject(token).equals(username)) {
            throw new UnauthorizedException("you are not allowed to perform this operation");
        }
        return ManagementExistsDto.builder().exists(this.managesRepository.findManagement(username, venueId).isPresent()).build();
    }

    private UserManagerOptionDto convertToManagerDto(User manager) {
        return new UserManagerOptionDto(
                manager.getId(),
                manager.getUsername()
        );
    }
}
