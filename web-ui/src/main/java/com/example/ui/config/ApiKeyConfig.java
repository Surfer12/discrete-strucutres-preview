package com.example.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {

    @Value("${MISTRAL_API_KEY:}")
    private String mistralApiKey;

    @Value("${OPENAI_API_KEY:}")
    private String openaiApiKey;

    @Value("${NOVA_API_KEY:}")
    private String novaApiKey;

    public String getMistralApiKey() {
        return mistralApiKey;
    }

    public String getOpenaiApiKey() {
        return openaiApiKey;
    }

    public String getNovaApiKey() {
        return novaApiKey;
    }
}