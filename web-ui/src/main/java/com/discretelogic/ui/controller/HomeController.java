package com.discretelogic.ui.controller;

import com.discretelogic.ui.service.LogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Main controller for the Discrete Logic Web UI.
 * Handles home page and basic discrete logic operations.
 */
@Controller
public class HomeController {

    @Autowired
    private LogicService logicService;

    @Value("${app.title:Discrete Logic Mathematics}")
    private String appTitle;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    /**
     * Home page - displays the main interface
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", appTitle);
        model.addAttribute("version", appVersion);
        model.addAttribute("greeting", "Welcome to the Discrete Logic Mathematics Web Interface!");
        model.addAttribute("description", "Explore discrete logic concepts, perform calculations, and visualize mathematical structures.");
        return "index";
    }

    /**
     * Logic Calculator page
     */
    @GetMapping("/calculator")
    public String calculator(Model model) {
        model.addAttribute("title", "Logic Calculator");
        model.addAttribute("pageTitle", "Logic Calculator");
        return "calculator";
    }

    /**
     * Process logic calculation
     */
    @PostMapping("/calculate")
    public String calculate(@RequestParam("expression") String expression, Model model) {
        model.addAttribute("title", "Logic Calculator");
        model.addAttribute("pageTitle", "Logic Calculator - Result");
        model.addAttribute("expression", expression);
        
        // Use the LogicService to evaluate the expression
        String result = logicService.evaluateExpression(expression);
        model.addAttribute("result", result);
        
        return "calculator";
    }

    /**
     * Truth Tables page
     */
    @GetMapping("/truth-tables")
    public String truthTables(Model model) {
        model.addAttribute("title", "Truth Tables");
        model.addAttribute("pageTitle", "Truth Tables Generator");
        return "truth-tables";
    }

    /**
     * About page
     */
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
        model.addAttribute("pageTitle", "About Discrete Logic Mathematics");
        return "about";
    }
}