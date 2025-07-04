package com.discretelogic.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for managing API keys and external service configurations.
 * This class handles environment variables for Mistral, OpenAI, and Amazon Nova APIs.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApiConfiguration {

    @Value("${mistral.api.key}")
    private String mistralApiKey;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${amazon.nova.api.key}")
    private String amazonNovaApiKey;

    @Value("${app.title}")
    private String appTitle;

    @Value("${app.version}")
    private String appVersion;

    @Value("${app.description}")
    private String appDescription;

    /**
     * Get the Mistral API key
     * @return The Mistral API key from environment variables
     */
    public String getMistralApiKey() {
        return mistralApiKey;
    }

    /**
     * Get the OpenAI API key
     * @return The OpenAI API key from environment variables
     */
    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    /**
     * Get the Amazon Nova API key
     * @return The Amazon Nova API key from environment variables
     */
    public String getAmazonNovaApiKey() {
        return amazonNovaApiKey;
    }

    /**
     * Get the application title
     * @return The application title
     */
    public String getAppTitle() {
        return appTitle;
    }

    /**
     * Get the application version
     * @return The application version
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Get the application description
     * @return The application description
     */
    public String getAppDescription() {
        return appDescription;
    }

    /**
     * Check if API keys are properly configured
     * @return true if all required API keys are set
     */
    public boolean areApiKeysConfigured() {
        return isValidApiKey(mistralApiKey) && 
               isValidApiKey(openAiApiKey) && 
               isValidApiKey(amazonNovaApiKey);
    }

    /**
     * Check if a single API key is valid
     * @param apiKey The API key to validate
     * @return true if the API key is valid
     */
    private boolean isValidApiKey(String apiKey) {
        return apiKey != null && 
               !apiKey.trim().isEmpty() && 
               !apiKey.startsWith("your-") && 
               !apiKey.equals("your-mistral-api-key") &&
               !apiKey.equals("your-openai-api-key") &&
               !apiKey.equals("your-nova-api-key");
    }

    /**
     * Get configuration status for debugging
     * @return A string representation of the configuration status
     */
    public String getConfigurationStatus() {
        return String.format(
            "API Configuration Status: Mistral=%s, OpenAI=%s, Nova=%s",
            isValidApiKey(mistralApiKey) ? "✓" : "✗",
            isValidApiKey(openAiApiKey) ? "✓" : "✗",
            isValidApiKey(amazonNovaApiKey) ? "✓" : "✗"
        );
    }
}