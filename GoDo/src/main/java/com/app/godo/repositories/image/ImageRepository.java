package com.app.godo.repositories.image;

import com.app.godo.models.Image;
import com.app.godo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByProfileImageOf(User profileImageOf);
}
