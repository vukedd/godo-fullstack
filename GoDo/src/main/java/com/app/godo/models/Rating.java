package com.app.godo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int performance;

    @Column
    private int ambient;

    @Column
    private int venue;

    @Column
    private int overallImpression;

    @OneToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}
