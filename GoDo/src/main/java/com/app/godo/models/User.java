package com.app.godo.models;

import com.app.godo.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("MEMBER")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "memberSince", nullable = false)
    private LocalDate memberSince;

    @Column(name = "dateOfBirth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "phoneNumber", nullable = true, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "city", nullable = true)
    private String city;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "profileStatus", nullable = false)
    private ProfileStatus profileStatus;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image profileImage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manager")
    private List<Manages> manages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewedBy")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentedBy")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Column(name = "role", insertable = false, updatable = false)
    private String role;

}
