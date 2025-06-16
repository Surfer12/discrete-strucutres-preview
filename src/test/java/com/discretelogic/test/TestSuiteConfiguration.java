package com.discretelogic.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Comprehensive test suite configuration for the Discrete Logic project.
 * Provides centralized test setup, logging, and mocking capabilities.
 */
@ExtendWith(MockitoExtension.class)
public class TestSuiteConfiguration {
    private static final Logger GLOBAL_LOGGER = LoggerFactory.getLogger(TestSuiteConfiguration.class);

    /**
     * Log test method entry with detailed information.
     *
     * @param testClass The test class
     * @param testMethod The test method being executed
     */
    protected void logTestEntry(Class<?> testClass, Method testMethod) {
        GLOBAL_LOGGER.info("Executing test: {} in class {}", 
            testMethod.getName(), testClass.getSimpleName());
    }

    /**
     * Log test method exit with result status.
     *
     * @param testClass The test class
     * @param testMethod The test method that was executed
     * @param passed Whether the test passed
     */
    protected void logTestExit(Class<?> testClass, Method testMethod, boolean passed) {
        GLOBAL_LOGGER.info("Test {} in class {} - Status: {}", 
            testMethod.getName(), testClass.getSimpleName(), 
            passed ? "PASSED" : "FAILED");
    }

    /**
     * Centralized error logging for test failures.
     *
     * @param testClass The test class
     * @param testMethod The test method
     * @param error The error that occurred
     */
    protected void logTestError(Class<?> testClass, Method testMethod, Throwable error) {
        GLOBAL_LOGGER.error("Test {} in class {} failed with error: {}", 
            testMethod.getName(), testClass.getSimpleName(), error.getMessage(), error);
    }

    /**
     * Provides a standardized way to create loggers for test classes.
     *
     * @param testClass The test class requiring a logger
     * @return A configured SLF4J Logger
     */
    protected Logger getTestLogger(Class<?> testClass) {
        return LoggerFactory.getLogger(testClass);
    }
} 