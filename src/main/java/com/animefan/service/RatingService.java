package com.animefan.service;

import com.animefan.model.Rating;
import com.animefan.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public List<Rating> getByAnimeId(Long animeId) {
        return ratingRepository.findAll().stream()
                .filter(r -> r.getAnime() != null && r.getAnime().getId().equals(animeId))
                .toList();
    }
}
