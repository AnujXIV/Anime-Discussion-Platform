package com.animefan.service;

import com.animefan.model.Character;
import com.animefan.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;

    public List<Character> getAll() {
        return characterRepository.findAll();
    }

    public Character getById(Long id) {
        return characterRepository.findById(id).orElse(null);
    }

    public Character save(Character character) {
        return characterRepository.save(character);
    }

    public void delete(Long id) {
        characterRepository.deleteById(id);
    }

    public List<Character> getByAnimeName(String animeName) {
        return characterRepository.findByAnime_NameContainingIgnoreCase(animeName);
    }

    public Page<Character> getPagedCharacters(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return characterRepository.findAll(pageable);
    }
}
