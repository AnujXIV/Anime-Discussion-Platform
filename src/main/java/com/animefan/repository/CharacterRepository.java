package com.animefan.repository;

import com.animefan.model.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findByAnime_NameContainingIgnoreCase(String animeName);
    Page<Character> findAll(Pageable pageable);
    List<Character> findByAnimeId(Long animeId);
}
