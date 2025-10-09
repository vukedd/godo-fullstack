package com.app.godo.controllers.user;

import com.app.godo.dtos.auth.PasswordChangeRequest;
import com.app.godo.dtos.user.UserDetailsDto;
import com.app.godo.dtos.user.UserManagerOptionDto;
import com.app.godo.dtos.user.UserProfileDto;
import com.app.godo.services.user.UserService;
import com.app.godo.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Utils utils;

    @PatchMapping(value = "update", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateUserDetails(
            @RequestPart("userDetails") String userJson,
            @RequestPart("image") MultipartFile imageFile,
            @RequestHeader("Authorization") String authHeader) {

        userService.finishUserDetails(userService.convertToUserDetailsDto(userJson), imageFile, utils.extractToken(authHeader));
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/details/{username}")
    public ResponseEntity<UserDetailsDto> getUserDetails(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(userService.getUserDetailsFormDataByUsername(username, utils.extractToken(authHeader)));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfileInformation(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(userService.getUserProfileInformation(utils.extractToken(authHeader)));
    }

    @PatchMapping("/edit-password/{username}")
    public ResponseEntity<Void> changePassword(
            @PathVariable String username,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PasswordChangeRequest body
    ) {
        userService.changePasswordByUsername(username, body, utils.extractToken(authHeader));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/edit-profile", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> changeProfile(
            @RequestPart("userDetails") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestHeader("Authorization") String authHeader
    ) {
        MultipartFile file =  imageFile != null && !imageFile.isEmpty() ? imageFile : null;
        userService.changeProfileDetails(userService.convertToEditUserProfileDto(userJson), file, utils.extractToken(authHeader));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/manager/options")
    public ResponseEntity<List<UserManagerOptionDto>> getManagerOptions(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok(userService.getManagerOptions(utils.extractToken(authHeader)));
    }

}
