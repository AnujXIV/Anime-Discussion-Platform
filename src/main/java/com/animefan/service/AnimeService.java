package com.animefan.service;

import com.animefan.model.Anime;
import com.animefan.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> getAll() {
        return animeRepository.findAll();
    }

    public Anime getById(Long id) {
        return animeRepository.findById(id).orElse(null);
    }

    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public void delete(Long id) {
        animeRepository.deleteById(id);
    }

    public List<Anime> getByGenre(String genre) {
        return animeRepository.findByGenresContainingIgnoreCase(genre);
    }

    public Page<Anime> getPagedAnime(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return animeRepository.findAll(pageable);
    }

    public Page<Anime> getPagedAnimeByGenre(String genre, int page, int size) {
        List<Anime> filtered = getByGenre(genre);
        int total = filtered.size();
        int start = page * size;

        if (start >= total) {
            return new PageImpl<>(List.of(), PageRequest.of(page, size), total);
        }

        int end = Math.min(start + size, total);
        return new PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), total);
    }

    public Page<Anime> getPagedAnimeByGenres(List<String> genres, int page, int size) {
        if (genres == null || genres.isEmpty()) {
            return getPagedAnime(page, size);
        }

        List<Anime> all = animeRepository.findAll();

        List<Anime> filtered = all.stream()
                .filter(anime -> {
                    if (anime.getGenres() == null) return false;

                    List<String> animeGenres = Arrays.stream(anime.getGenres().split(","))
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .toList();

                    return genres.stream()
                            .map(String::toLowerCase)
                            .allMatch(animeGenres::contains); // ✅ Must match ALL selected genres
                })
                .toList();

        int total = filtered.size();
        int start = page * size;
        int end = Math.min(start + size, total);

        if (start >= total) return new PageImpl<>(List.of(), PageRequest.of(page, size), total);
        return new PageImpl<>(filtered.subList(start, end), PageRequest.of(page, size), total);
    }

}
