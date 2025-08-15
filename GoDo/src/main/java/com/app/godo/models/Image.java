package com.app.godo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = false, nullable = false)
    private String path;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User profileImageOf;

    @OneToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id")
    private Venue venueImageOf;

    @OneToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Event eventImageOf;
}
