package com.animefan.controller;

import com.animefan.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/characters")
@RequiredArgsConstructor
@Slf4j
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public String characters(Model model) {
        model.addAttribute("characters", characterService.getAll()); // For initial render fallback
        log.info("👥 Loaded character list ({} characters)", characterService.getAll().size());
        return "character_list";
    }

    @GetMapping("/{id}")
    public String characterDetail(@PathVariable Long id, Model model) {
        var character = characterService.getById(id);
        if (character == null) {
            log.warn("⚠️ Character not found for ID: {}", id);
            return "redirect:/characters?error=notfound";
        }
        model.addAttribute("character", character);
        log.info("🎭 Loaded character detail: {}", character.getName());
        return "character_detail";
    }
}
