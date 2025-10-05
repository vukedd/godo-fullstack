package com.app.godo.dtos.user;

import com.app.godo.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    String username;
    String email;
    String phoneNumber;
    String address;
    String city;
    LocalDate dateOfBirth;
    String imagePath;

    public static UserProfileDto fromEntity(User user) {
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .city(user.getCity())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();

        if (user.getProfileImage() != null) {
            userProfileDto.setImagePath(user.getProfileImage().getPath());
        } else {
            userProfileDto.setImagePath("");
        }

        return userProfileDto;
    }
}