package com.discretelogic.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application for the Discrete Logic Web UI.
 * This application provides a web interface for the discrete logic mathematics library.
 */
@SpringBootApplication(scanBasePackages = { "com.discretelogic.ui", "com.discretelogic" })
public class UiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }
}