package com.example.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // TODO: Replace with a call to your agent or back-end service.
        model.addAttribute("greeting", "Hello, World!");
        return "index"; // Renders templates/index.html
    }
}