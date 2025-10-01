package com.app.godo.repositories.user;

import com.app.godo.dtos.accountRequest.AccountRequestDto;
import com.app.godo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByEmail(String email);

    Optional<User>  findByPhoneNumber(String phoneNumber);
}
