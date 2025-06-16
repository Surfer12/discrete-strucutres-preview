package com.discretelogic.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility for resolving path aliases, particularly @/src references.
 */
public class PathResolver {
    private static final String SRC_ALIAS = "@/src";
    private static final String BASE_PATH = System.getProperty("user.dir");

    /**
     * Resolves a path that starts with @/src to its full system path.
     * 
     * @param pathString The path string to resolve
     * @return The fully resolved absolute path
     */
    public static String resolvePath(String pathString) {
        if (pathString == null) {
            return null;
        }

        if (pathString.startsWith(SRC_ALIAS)) {
            String relativePath = pathString.replace(SRC_ALIAS, "");
            return Paths.get(BASE_PATH, "src", "main", "java", relativePath.startsWith("/") ? relativePath.substring(1) : relativePath)
                        .toAbsolutePath()
                        .toString();
        }

        return pathString;
    }

    /**
     * Converts a path to a File object, resolving @/src aliases.
     * 
     * @param pathString The path string to convert
     * @return A File representing the resolved path
     */
    public static File resolveFile(String pathString) {
        return new File(resolvePath(pathString));
    }

    /**
     * Converts a path to a Path object, resolving @/src aliases.
     * 
     * @param pathString The path string to convert
     * @return A Path representing the resolved path
     */
    public static Path resolvePaths(String pathString) {
        return Paths.get(resolvePath(pathString));
    }
} 