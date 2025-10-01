package com.app.godo.controllers.user;

import com.app.godo.dtos.user.UserDetailsDto;
import com.app.godo.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping(value = "update", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateUserDetails(
            @RequestPart("userDetails") String userJson,
            @RequestPart("image") MultipartFile imageFile,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        userService.finishUserDetails(userService.convertToUserDetailsDto(userJson), imageFile, token);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/details/{username}")
    public ResponseEntity<UserDetailsDto> getUserDetails(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(userService.getUserDetailsFormDataByUsername(username, token));
    }
}
