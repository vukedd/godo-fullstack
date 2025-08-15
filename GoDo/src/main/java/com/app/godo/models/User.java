package com.app.godo.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
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

    @Column(name = "dateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @OneToOne
    private Image profileImage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "manager")
    private List<Manages> manages;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reviewedBy")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentedBy")
    private List<Comment> comments;
}
