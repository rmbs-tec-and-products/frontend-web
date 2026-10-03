package br.com.migracao.frontend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/")
    String home(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("authorities", authentication.getAuthorities());
        return "home";
    }

    @GetMapping("/admin")
    String admin(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "admin/index";
    }
}
