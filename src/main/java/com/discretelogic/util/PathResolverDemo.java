package com.discretelogic.util;

import java.io.File;
import java.nio.file.Path;

/**
 * Demonstrates the usage of PathResolver for @/src path resolution.
 */
public class PathResolverDemo {
    public static void main(String[] args) {
        // Example path resolution
        String srcPath = "@/src/com/discretelogic/app/DiscreteLogicCLI.java";
        
        // Resolve to string path
        String resolvedPath = PathResolver.resolvePath(srcPath);
        System.out.println("Resolved Path (String): " + resolvedPath);
        
        // Resolve to File
        File resolvedFile = PathResolver.resolveFile(srcPath);
        System.out.println("Resolved Path (File): " + resolvedFile.getAbsolutePath());
        
        // Resolve to Path
        Path resolvedNioPath = PathResolver.resolvePaths(srcPath);
        System.out.println("Resolved Path (NIO Path): " + resolvedNioPath.toString());
    }
} 