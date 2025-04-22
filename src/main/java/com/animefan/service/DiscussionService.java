package com.animefan.service;

import com.animefan.model.Anime;
import com.animefan.model.Discussion;
import com.animefan.model.Character;
import com.animefan.repository.AnimeRepository;
import com.animefan.repository.CharacterRepository;
import com.animefan.repository.DiscussionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final AnimeRepository animeRepository;
    private final CharacterRepository characterRepository;

    public List<Discussion> getAll() {
        return discussionRepository.findAll();
    }

    public List<Discussion> getAnimeOnlyDiscussions(Long animeId) {
        return discussionRepository.findAll().stream()
                .filter(d -> d.getAnime() != null &&
                        d.getAnime().getId().equals(animeId) &&
                        d.getCharacter() == null)
                .toList();
    }

    public List<Discussion> getCharacterDiscussions(Long animeId, Long characterId) {
        return discussionRepository.findAll().stream()
                .filter(d -> d.getAnime() != null &&
                        d.getCharacter() != null &&
                        d.getAnime().getId().equals(animeId) &&
                        d.getCharacter().getId().equals(characterId))
                .toList();
    }

    public List<Discussion> getByUsername(String username) {
        return discussionRepository.findAll().stream()
                .filter(d -> d.getUser() != null &&
                        d.getUser().getUsername().equalsIgnoreCase(username))
                .toList();
    }

    public Discussion save(Discussion discussion) {
        return discussionRepository.save(discussion);
    }

    public void delete(Long id) {
        discussionRepository.deleteById(id);
    }

    public List<Discussion> getCharacterDiscussionsForAnime(Long animeId) {
        return discussionRepository.findAll().stream()
                .filter(d -> d.getAnime() != null &&
                        d.getCharacter() != null &&
                        d.getAnime().getId().equals(animeId))
                .toList();
    }

    public Discussion getById(Long id) {
        return discussionRepository.findById(id).orElse(null);
    }

    public Anime getAnimeById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    public Character getCharacterById(Long id) {
        return characterRepository.findById(id).orElse(null);
    }
}
