package com.app.godo.repositories.user;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
