package com.animefan.rest;

import com.animefan.model.Rating;
import com.animefan.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
@Slf4j
public class RatingRestController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAll() {
        log.info("⭐ Fetching all ratings");
        return ResponseEntity.ok(ratingService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody Rating rating) {
        try {
            Rating saved = ratingService.save(rating);
            log.info("✅ Rating added for anime ID: {}", saved.getAnime().getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("❌ Failed to save rating", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save rating");
        }
    }

    @GetMapping("/anime/{animeId}")
    public ResponseEntity<List<Rating>> getByAnime(@PathVariable Long animeId) {
        log.info("🎯 Fetching ratings for anime ID: {}", animeId);
        return ResponseEntity.ok(ratingService.getByAnimeId(animeId));
    }
}
