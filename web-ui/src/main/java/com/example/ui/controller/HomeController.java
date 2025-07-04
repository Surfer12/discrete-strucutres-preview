package com.example.ui.controller;

import com.example.ui.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    private final AgentService agentService;
    
    @Autowired
    public HomeController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/")
    public String home(Model model) {
        // Check if API keys are configured
        Map<String, Boolean> providers = agentService.getAvailableProviders();
        boolean hasApiKeys = providers.values().stream().anyMatch(Boolean::booleanValue);
        
        model.addAttribute("greeting", "Welcome to the Agent UI");
        model.addAttribute("hasApiKeys", hasApiKeys);
        model.addAttribute("availableProviders", providers);
        model.addAttribute("features", new String[]{
            "Discrete Logic Mathematics Tools",
            "AI Agent Integration",
            "Multiple LLM Support (Mistral, OpenAI, Nova)"
        });
        
        return "index";
    }
    
    @PostMapping("/api/agent/query")
    @ResponseBody
    public Map<String, Object> queryAgent(@RequestParam String prompt,
                                          @RequestParam(defaultValue = "openai") String provider) {
        // Convert provider string to enum
        AgentService.ModelProvider modelProvider;
        try {
            modelProvider = AgentService.ModelProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to OpenAI if invalid provider
            modelProvider = AgentService.ModelProvider.OPENAI;
        }
        
        return agentService.queryAgent(prompt, modelProvider);
    }
}