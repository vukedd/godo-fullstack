package com.app.godo.dtos.user;

import com.app.godo.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagerOptionDto {
    private long id;
    private String username;

    public static UserManagerOptionDto fromEntity(User user) {
        UserManagerOptionDto dto = new UserManagerOptionDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
