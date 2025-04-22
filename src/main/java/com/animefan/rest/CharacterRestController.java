package com.animefan.rest;

import com.animefan.model.Character;
import com.animefan.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
@Slf4j
public class CharacterRestController {

    private final CharacterService characterService;

    @GetMapping
    public ResponseEntity<?> getAllCharacters(
            @RequestParam(required = false) String anime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try{
            if (anime != null && !anime.isBlank()) {
                log.info("🔍 Filtering characters by anime: {}", anime);
                List<Character> filtered = characterService.getByAnimeName(anime);
                return ResponseEntity.ok(filtered);
            }

            log.info("📦 Paginating characters - page: {}, size: {}", page, size);
            Page<Character> pagedResult = characterService.getPagedCharacters(page, size);
            return ResponseEntity.ok(Map.of(
                    "totalElements", pagedResult.getTotalElements(),
                    "totalPages", pagedResult.getTotalPages(),
                    "currentPage", page,
                    "content", pagedResult.getContent()
            ));
        } catch (Exception e) {
            log.error("🔥 Error fetching characters", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to load characters list"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacter(@PathVariable Long id) {
        Character character = characterService.getById(id);
        return character != null
                ? ResponseEntity.ok(character)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found");
    }

    @PostMapping
    public ResponseEntity<?> addCharacter(@RequestBody Character character) {
        try {
            Character saved = characterService.save(character);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("❌ Error adding character", e);
            return ResponseEntity.badRequest().body("Failed to add character");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(@PathVariable Long id, @RequestBody Character character) {
        try {
            character.setId(id);
            Character updated = characterService.save(character);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("❌ Error updating character", e);
            return ResponseEntity.badRequest().body("Failed to update character");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable Long id) {
        try {
            characterService.delete(id);
            return ResponseEntity.ok("Character deleted");
        } catch (Exception e) {
            log.error("❌ Error deleting character", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Character not found");
        }
    }
}
