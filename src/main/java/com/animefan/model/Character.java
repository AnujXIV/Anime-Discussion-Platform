package com.animefan.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "anime_character")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int age;
    private String birthdate;
    private String height;


    @ManyToOne
    @JoinColumn(name = "anime_id")
    @JsonIgnoreProperties({"characters", "discussions", "ratings"})
    private Anime anime;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Discussion> discussions;

    // 🔄 Helper method to get the image path dynamically
    public String getImagePath() {
        return "/images/CharacterThumbnail_" + this.id + ".jpg";
    }
}
