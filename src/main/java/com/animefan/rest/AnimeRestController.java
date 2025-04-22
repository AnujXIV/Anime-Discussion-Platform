package com.animefan.rest;

import com.animefan.model.Anime;
import com.animefan.repository.AnimeRepository;
import com.animefan.repository.CharacterRepository;
import com.animefan.repository.DiscussionRepository;
import com.animefan.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/anime")
@RequiredArgsConstructor
@Slf4j
public class AnimeRestController {

    private final AnimeService animeService;
    private final CharacterRepository characterRepository;
    private final AnimeRepository animeRepository;
    private final DiscussionRepository discussionRepository;

    @GetMapping
    public ResponseEntity<?> getAllAnime(
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            log.info("🎬 Fetching anime list: genre='{}', page={}, size={}", genre, page, size);

            Page<Anime> result;
            if (genre != null && !genre.isBlank()) {
                // multiple genres expected, separated by commas
                List<String> genreList = Arrays.stream(genre.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
                result = animeService.getPagedAnimeByGenres(genreList, page, size);
            } else {
                result = animeService.getPagedAnime(page, size);
            }

            return ResponseEntity.ok(Map.of(
                    "totalElements", result.getTotalElements(),
                    "totalPages", result.getTotalPages(),
                    "pageable", result.getPageable(),
                    "content", result.getContent()
            ));
        } catch (Exception e) {
            log.error("🔥 Error fetching paged anime", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to load anime list"));
        }
    }




    @GetMapping("/genres")
    public ResponseEntity<List<String>> getGenres() {
        List<Anime> allAnime = animeService.getAll();
        Set<String> genreSet = allAnime.stream()
                .flatMap(anime -> {
                    if (anime.getGenres() != null) {
                        return Arrays.stream(anime.getGenres().split(","));
                    }
                    return Stream.empty();
                })
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(TreeSet::new)); // sorted and unique

        log.info("🎭 Extracted {} unique genres", genreSet.size());
        return ResponseEntity.ok(new ArrayList<>(genreSet));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnimeById(@PathVariable Long id) {
        Anime anime = animeRepository.findById(id).orElse(null);

        if (anime == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Anime not found"));
        }

        // Eagerly load characters & discussions (if not already fetched)
        anime.setCharacters(characterRepository.findByAnimeId(id));
        anime.setDiscussions(discussionRepository.findByAnimeId(id)); // You might need to implement this

        return ResponseEntity.ok(anime);
    }


    @PostMapping
    public ResponseEntity<?> addAnime(@RequestBody Anime anime) {
        try {
            Anime saved = animeService.save(anime);
            log.info("✅ Anime saved: {}", saved.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("❌ Error saving anime", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating anime");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnime(@PathVariable Long id, @RequestBody Anime updated) {
        try {
            updated.setId(id);
            Anime saved = animeService.save(updated);
            log.info("✏️ Anime updated: {}", saved.getName());
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("❌ Error updating anime", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating anime");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnime(@PathVariable Long id) {
        try {
            animeService.delete(id);
            log.info("🗑️ Anime deleted with ID: {}", id);
            return ResponseEntity.ok("Anime deleted");
        } catch (Exception e) {
            log.error("❌ Error deleting anime with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Anime not found");
        }
    }
}
