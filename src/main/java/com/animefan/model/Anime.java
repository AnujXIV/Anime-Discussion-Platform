package com.animefan.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String tags;
    private String status;
    private String rating;
    private String genres;
    private String studios;
    private String origin;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Character> characters;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Discussion> discussions;

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Rating> ratings;

    // 🔄 Helper method to get the image path dynamically
    public String getImagePath() {
        return "/images/AnimeThumbnail_" + this.id + ".jpg";
    }
}
