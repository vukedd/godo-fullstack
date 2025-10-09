package com.app.godo.services.user;

import com.app.godo.dtos.auth.PasswordChangeRequest;
import com.app.godo.dtos.user.EditUserProfileDto;
import com.app.godo.dtos.user.UserDetailsDto;
import com.app.godo.dtos.user.UserManagerOptionDto;
import com.app.godo.dtos.user.UserProfileDto;
import com.app.godo.enums.ProfileStatus;
import com.app.godo.exceptions.general.ConflictException;
import com.app.godo.exceptions.general.NotFoundException;
import com.app.godo.exceptions.general.ParseException;
import com.app.godo.exceptions.general.UnauthorizedException;
import com.app.godo.models.Image;
import com.app.godo.models.User;
import com.app.godo.repositories.image.ImageRepository;
import com.app.godo.repositories.user.UserRepository;
import com.app.godo.services.auth.JwtService;
import com.app.godo.services.email.EmailService;
import com.app.godo.services.files.FileStorageService;
import com.app.godo.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${app.host.url}")
    private String hostUrl;

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;
    private final EmailService emailService;

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;

    @Transactional
    public void finishUserDetails(UserDetailsDto userDetails, MultipartFile userPfp, String token) {
        String subject = utils.extractSubject(token);

        if (!subject.equals(userDetails.getUsername())) {
            throw new UnauthorizedException("you are not allowed to perform this operation");
        }

        boolean userWithSamePhoneNumberExists = userRepository.findByPhoneNumber(userDetails.getPhoneNumber()).isPresent();

        if (userWithSamePhoneNumberExists) {
            throw new ConflictException("phone number is already taken");
        }

        User user = userRepository.findByUsername(userDetails
                .getUsername()).orElseThrow(() -> new NotFoundException("the user you were looking for cant be found!"));


        user.setAddress(userDetails.getAddress());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setCity(userDetails.getCity());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        user.setProfileStatus(ProfileStatus.COMPLETED);

        Image image = imageRepository.findByProfileImageOf(user);

        String path = hostUrl + "/uploads/" + fileStorageService.storeFile(userPfp);;

        user.setProfileImage(
                Image.builder()
                        .profileImageOf(user)
                        .path(path).build()
        );

        userRepository.save(user);
    }

    public List<UserManagerOptionDto> getManagerOptions(String token) {
        String role = utils.extractRole(token);

        if (role.equals("MEMBER")) {
            throw new UnauthorizedException("you are not allowed to perform this action");
        }

        return this.userRepository
                .findAll()
                .stream()
                .filter(user -> user.getRole().equals("MEMBER"))
                .map(UserManagerOptionDto::fromEntity)
                .collect(Collectors.toList());
    }

    public UserDetailsDto getUserDetailsFormDataByUsername(String username, String token) {
        String subject = utils.extractSubject(token);

        if (!subject.equals(username)) {
            throw new UnauthorizedException("you are not allowed to perform this operation");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("the user you were looking for cant be found!"));

        return UserDetailsDto
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public UserProfileDto getUserProfileInformation(String token) {
        String subject = utils.extractSubject(token);

        User user = userRepository.findByUsername(subject)
                .orElseThrow(() -> new NotFoundException("the user you were looking for can't be found!"));


        return UserProfileDto.fromEntity(user);
    }

    public void changePasswordByUsername(String username, PasswordChangeRequest request, String token) {
        String subject = utils.extractSubject(token);

        if (!subject.equals(username)) {
            throw new UnauthorizedException("you are not allowed to perform this operation");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("the user you were looking for cant be found!"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ConflictException("old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        emailService.sendPasswordChangeEmail(user.getUsername(), user.getEmail());
    }

    @Transactional
    public void changeProfileDetails(EditUserProfileDto editUserProfileDto, MultipartFile file, String token) {
        String subject = utils.extractSubject(token);

        Optional<User> userWithSamePhoneCheck = userRepository.findByPhoneNumber(editUserProfileDto.getPhoneNumber());
        if (userRepository.findByPhoneNumber(editUserProfileDto.getPhoneNumber()).isPresent() && !  userWithSamePhoneCheck.get().getUsername().equals(subject)) {
            throw new ConflictException("the entered phone number is already taken");
        }

        User user = userRepository.findByUsername(subject)
                .orElseThrow(() -> new NotFoundException("the user you were looking for cant be found!"));

        user.setAddress(editUserProfileDto.getAddress());
        user.setPhoneNumber(editUserProfileDto.getPhoneNumber());
        user.setCity(editUserProfileDto.getCity());
        user.setDateOfBirth(editUserProfileDto.getDateOfBirth());

        if (file != null) {
            Image oldImage = user.getProfileImage();

            String path = hostUrl + "/uploads/" + fileStorageService.storeFile(file);

            Image newImage = Image.builder().path(path).build();
            user.setProfileImage(newImage);

            if (oldImage != null) {
                fileStorageService.delete(oldImage.getPath());
            }
        }

        userRepository.save(user);

    }

    public UserDetailsDto convertToUserDetailsDto(String userJson) {
        UserDetailsDto user;
        try {
            user = objectMapper.readValue(userJson, UserDetailsDto.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("An expected error has occurred please try again in a moment!");
        }

        return user;
    }

    public EditUserProfileDto convertToEditUserProfileDto(String userJson) {
        EditUserProfileDto user;
        try {
            user = objectMapper.readValue(userJson, EditUserProfileDto.class);
        } catch (JsonProcessingException e) {
            throw new ParseException("An expected error has occurred please try again in a moment!");
        }

        return user;
    }
}