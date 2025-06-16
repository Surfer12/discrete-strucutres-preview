package com.discretelogic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Centralized logging utility for the Discrete Logic project.
 * Provides consistent logging mechanisms across different components.
 */
public final class LoggingUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private LoggingUtil() {
        throw new AssertionError("LoggingUtil cannot be instantiated");
    }

    /**
     * Create a logger for the given class.
     *
     * @param clazz The class for which the logger is being created
     * @return A configured SLF4J Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Log an error with a formatted message and optional arguments.
     *
     * @param logger The logger to use
     * @param message The error message template
     * @param args Optional arguments for the message
     */
    public static void error(Logger logger, String message, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(message, args);
        }
    }

    /**
     * Log a warning with a formatted message and optional arguments.
     *
     * @param logger The logger to use
     * @param message The warning message template
     * @param args Optional arguments for the message
     */
    public static void warn(Logger logger, String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }

    /**
     * Log an info message with a formatted message and optional arguments.
     *
     * @param logger The logger to use
     * @param message The info message template
     * @param args Optional arguments for the message
     */
    public static void info(Logger logger, String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    /**
     * Log a debug message with a formatted message and optional arguments.
     *
     * @param logger The logger to use
     * @param message The debug message template
     * @param args Optional arguments for the message
     */
    public static void debug(Logger logger, String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    /**
     * Log a trace message with a formatted message and optional arguments.
     *
     * @param logger The logger to use
     * @param message The trace message template
     * @param args Optional arguments for the message
     */
    public static void trace(Logger logger, String message, Object... args) {
        if (logger.isTraceEnabled()) {
            logger.trace(message, args);
        }
    }

    public static void logInfo(Logger logger, String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public static void logError(Logger logger, String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error(message, throwable);
        }
    }
} 