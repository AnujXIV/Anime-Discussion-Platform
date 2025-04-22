package com.animefan.controller;

import com.animefan.model.Anime;
import com.animefan.service.AnimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/anime")
@RequiredArgsConstructor
@Slf4j
public class AnimeController {

    private final AnimeService animeService;

    // Load Thymeleaf view for Anime List page
    @GetMapping
    public String animeListPage(Model model) {
        model.addAttribute("animeList", animeService.getAll());
        log.info("📄 Rendering anime_list.html with {} animes", animeService.getAll().size());
        return "anime_list";
    }

    // Load Anime Detail page
    @GetMapping("/{id}")
    public String animeDetailPage(@PathVariable Long id, Model model) {
        Anime anime = animeService.getById(id);
        if (anime == null) {
            log.warn("⚠️ Anime not found for ID: {}", id);
            return "redirect:/anime?error=notfound";
        }

        model.addAttribute("anime", anime);
        log.info("📄 Rendering anime_detail.html for '{}'", anime.getName());
        return "anime_detail";
    }
}
