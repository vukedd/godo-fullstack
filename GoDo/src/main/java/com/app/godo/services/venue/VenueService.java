package com.app.godo.services.venue;

import com.app.godo.dtos.venue.CreateVenueRequestDto;
import com.app.godo.dtos.venue.UpdateVenueDto;
import com.app.godo.dtos.venue.VenueOverviewDto;
import com.app.godo.enums.VenueType;
import com.app.godo.exceptions.general.ConflictException;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.ParseException;
import com.app.godo.models.Image;
import com.app.godo.models.Venue;
import com.app.godo.repositories.venue.VenueRepository;
import com.app.godo.services.files.FileStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final FileStorageService fileStorageService;
    private final ObjectMapper objectMapper;

    public Page<VenueOverviewDto> filterVenues(String filter, int venueType, Pageable pageable) {
        Page<Venue> venues;

        if (venueType == -1) {
            venues = venueRepository.filterVenues(filter, filter, pageable);
        }
        else {
            venues = venueRepository.filterVenuesWithType(filter, filter, VenueType.values()[venueType], pageable);
        }

        return venues.map(VenueOverviewDto::fromEntity);
    }

    @Transactional
    public VenueOverviewDto createVenue(CreateVenueRequestDto venueRequest, MultipartFile venueImage) {
        Venue existingVenue = venueRepository.findVenueByName(venueRequest.getName());

        if (existingVenue != null) { throw new ConflictException("A venue with the entered name already exists!"); }

        Venue venue = Venue.builder()
                .name(venueRequest.getName())
                .description(venueRequest.getDescription())
                .address(venueRequest.getAddress())
                .type(venueRequest.getType())
                .averageRating(0)
                .createdAt(LocalDate.from(LocalDateTime.now()))
                .build();

        String path = "http://localhost:8080/uploads/" + fileStorageService.storeFile(venueImage);

        venue.setImage(
                Image.builder()
                .venueImageOf(venue)
                .path(path).build()
        );

        venueRepository.save(venue);

        return VenueOverviewDto.fromEntity(venue);
    }

    public CreateVenueRequestDto convertToCreateVenueRequest(String venueJson) {
        CreateVenueRequestDto venue;
        try {
            venue = objectMapper.readValue(venueJson, CreateVenueRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("An expected error has occurred please try again in a moment!");
        }

        return venue;
    }

    public VenueOverviewDto findVenueById(long venueId) {
        Venue venue = venueRepository.findVenueById(venueId)
                .orElseThrow(() -> new NotFoundException("The venue you were looking for can't be found"));

        return VenueOverviewDto.fromEntity(venue);
    }

    public UpdateVenueDto updateVenue(long venueId, UpdateVenueDto updateVenueDto) {
        Venue venue = venueRepository.findVenueById(venueId)
                .orElseThrow(() -> new NotFoundException("The venue you were looking for can't be found"));

        venue.setName(updateVenueDto.getName());
        venue.setAddress(updateVenueDto.getAddress());
        venue.setDescription(updateVenueDto.getDescription());
        venue.setType(updateVenueDto.getVenueType());

        venueRepository.save(venue);

        return UpdateVenueDto.fromEntity(venue);
    }

    public void deleteVenue(long venueId) {
        Venue venue = venueRepository.findVenueById(venueId)
                .orElseThrow(() -> new NotFoundException("The venue you were looking for can't be found"));

        venueRepository.delete(venue);
    }
}
