package com.animefan.repository;

import com.animefan.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByGenresContainingIgnoreCase(String genre);
    Page<Anime> findAll(Pageable pageable);
}
