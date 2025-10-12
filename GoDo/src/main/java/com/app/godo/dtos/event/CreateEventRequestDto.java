package com.app.godo.dtos.event;

import com.app.godo.enums.EventType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    private String description;
    private boolean recurrent;
    private EventType type;

    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @NotNull
    @DecimalMin(value = "1.0", inclusive = true, message = "Price must be greater than 0")
    @DecimalMax(value = "50000.0", message = "Price must be less than or equal to 50000")
    private Double price;

    private LocalDate date;
}
