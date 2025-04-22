package com.animefan.rest;

import com.animefan.model.Anime;
import com.animefan.model.Discussion;
import com.animefan.model.User;
import com.animefan.model.Character;
import com.animefan.service.AnimeService;
import com.animefan.service.CharacterService;
import com.animefan.service.DiscussionService;
import com.animefan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/discussions")
@RequiredArgsConstructor
@Slf4j
public class DiscussionRestController {

    private final DiscussionService discussionService;
    private final AnimeService animeService;
    private final CharacterService characterService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllDiscussions() {
        log.info("📢 Fetching all discussions");
        return ResponseEntity.ok(discussionService.getAll());
    }

    @GetMapping("/anime/{animeId}")
    public ResponseEntity<?> getAnimeOnlyDiscussions(@PathVariable Long animeId) {
        log.info("🎬 Fetching anime-only discussions for anime ID: {}", animeId);
        return ResponseEntity.ok(discussionService.getAnimeOnlyDiscussions(animeId));
    }

    @GetMapping("/anime/{animeId}/character/{characterId}")
    public ResponseEntity<?> getCharacterDiscussions(@PathVariable Long animeId, @PathVariable Long characterId) {
        log.info("🎭 Fetching character-specific discussions for anime ID: {} and character ID: {}", animeId, characterId);
        return ResponseEntity.ok(discussionService.getCharacterDiscussions(animeId, characterId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) {
        log.info("🧑 Fetching discussions by user: {}", username);
        return ResponseEntity.ok(discussionService.getByUsername(username));
    }

    @PostMapping
    public ResponseEntity<?> createDiscussion(@RequestBody Map<String, Object> payload) {
        try {
            String title = (String) payload.get("title");
            String description = (String) payload.get("description");
            Integer animeId = (Integer) payload.get("animeId"); // May be null
            Integer characterId = (Integer) payload.get("characterId"); // May be null

            if (title == null || description == null || animeId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid discussion data"));
            }

            Discussion discussion = new Discussion();
            discussion.setTitle(title);
            discussion.setDescription(description);
            discussion.setAnime(discussionService.getAnimeById(animeId.longValue()));

            if (characterId != null) {
                discussion.setCharacter(discussionService.getCharacterById(characterId.longValue()));
            }

            // Set current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String username = auth.getName();
                User user = userService.findByUsername(username);
                discussion.setUser(user);
            }

            Discussion saved = discussionService.save(discussion);
            log.info("✅ Discussion saved with ID {}", saved.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            log.error("❌ Failed to create discussion", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid discussion data"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscussion(@PathVariable Long id) {
        try {
            discussionService.delete(id);
            log.info("🗑️ Deleted discussion ID: {}", id);
            return ResponseEntity.ok("Discussion deleted");
        } catch (Exception e) {
            log.error("❌ Failed to delete discussion", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Discussion not found");
        }
    }
}
