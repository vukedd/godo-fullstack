package com.app.godo.models;

import com.app.godo.enums.EventType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean recurrent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false, referencedColumnName = "id")
    private Venue venue;

    @OneToOne(mappedBy = "eventImageOf", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Image image;
}
