package com.animefan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double storyRating;
    private double animation;
    private double visuals;
    private double artStyle;
    private double average;

    @ManyToOne
    @JoinColumn(name = "anime_id")
    @JsonBackReference
    private Anime anime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

}
