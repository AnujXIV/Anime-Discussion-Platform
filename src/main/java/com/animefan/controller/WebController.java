package com.animefan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Slf4j
public class WebController {

    @GetMapping("/index")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);
            log.info("🏠 Navigating to index for user: {}", username);
        } else {
            model.addAttribute("username", "Guest");
            log.warn("🚨 Unauthenticated access to index page - showing guest");
        }

        return "index";
    }
}
