package com.animefan.controller;

import com.animefan.model.Discussion;
import com.animefan.service.DiscussionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/discussions")
@RequiredArgsConstructor
@Slf4j
public class DiscussionController {

    private final DiscussionService discussionService;

    // ✅ Main global discussion page grouped by Anime and Character discussions
    @GetMapping
    public String showAllDiscussions(Model model) {
        log.info("📢 Loading global discussions page");

        List<Discussion> animeDiscussions = discussionService.getAll().stream()
                .filter(d -> d.getAnime() != null && d.getCharacter() == null)
                .toList();

        List<Discussion> characterDiscussions = discussionService.getAll().stream()
                .filter(d -> d.getAnime() != null && d.getCharacter() != null)
                .toList();

        model.addAttribute("animeDiscussions", animeDiscussions);
        model.addAttribute("characterDiscussions", characterDiscussions);

        return "discussion"; // discussion.html
    }

    // ✅ View a single discussion and enter chat
    @GetMapping("/{discussionId}")
    public String openDiscussionChat(@PathVariable Long discussionId, Model model) {
        Discussion discussion = discussionService.getById(discussionId);
        if (discussion == null) {
            log.warn("⚠️ Discussion ID {} not found", discussionId);
            return "redirect:/discussions";
        }

        log.info("💬 Opening chat for discussion ID {}", discussionId);
        model.addAttribute("discussion", discussion);
        return "discussion_chat";
    }
}
