package com.app.godo.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 20, message = "Username must be between 6 and 20 characters")
    private String username;

    @NotBlank(message = "E-mail is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 20, message = "City must be between 3 and 20 characters")
    private String city;

    @NotBlank(message = "Address is required")
    @Size(min = 2, max = 20, message = "Address must be between 3 and 20 characters")
    private String address;

    @Pattern(
            regexp = "^06\\d{7,8}$",
            message = "Phone number must start with 06 and have between 9-10 digits"
    )
    private String phoneNumber;
}
