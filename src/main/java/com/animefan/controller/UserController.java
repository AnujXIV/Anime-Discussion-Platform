package com.animefan.controller;

import com.animefan.model.User;
import com.animefan.repository.UserRepository;
import com.animefan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null); // ✅ safely fetched by username

        if (user == null) {
            log.warn("⚠️ User not found for username: {}", username);
            return "redirect:/login?error=usernotfound";
        }

        model.addAttribute("user", user);
        log.info("👤 Loaded profile for user: {}", username);
        return "user_profile"; // Make sure this Thymeleaf file exists
    }
}
