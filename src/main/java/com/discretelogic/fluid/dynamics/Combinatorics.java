package com.discretelogic.core;

import java.math.BigInteger;
import java.util.*;

/**
 * Provides combinatorial functions and calculations.
 */
public class Combinatorics {

    /**
     * Calculates factorial of n.
     */
    public static BigInteger factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }

        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * Calculates permutations P(n, r) = n! / (n-r)!
     */
    public static BigInteger permutations(int n, int r) {
        if (n < 0 || r < 0 || r > n) {
            throw new IllegalArgumentException("Invalid values for permutation calculation");
        }

        if (r == 0) return BigInteger.ONE;

        BigInteger result = BigInteger.ONE;
        for (int i = n; i > n - r; i--) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * Calculates combinations C(n, r) = n! / (r! * (n-r)!)
     */
    public static BigInteger combinations(int n, int r) {
        if (n < 0 || r < 0 || r > n) {
            throw new IllegalArgumentException("Invalid values for combination calculation");
        }

        if (r == 0 || r == n) return BigInteger.ONE;
        if (r > n - r) r = n - r; // Take advantage of symmetry

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < r; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
            result = result.divide(BigInteger.valueOf(i + 1));
        }
        return result;
    }

    /**
     * Generates all permutations of a given array.
     */
    public static <T> List<List<T>> generatePermutations(List<T> items) {
        List<List<T>> result = new ArrayList<>();
        generatePermutationsHelper(new ArrayList<>(items), 0, result);
        return result;
    }

    private static <T> void generatePermutationsHelper(List<T> items, int start, List<List<T>> result) {
        if (start == items.size()) {
            result.add(new ArrayList<>(items));
            return;
        }

        for (int i = start; i < items.size(); i++) {
            Collections.swap(items, start, i);
            generatePermutationsHelper(items, start + 1, result);
            Collections.swap(items, start, i); // backtrack
        }
    }

    /**
     * Generates all combinations of r items from the given list.
     */
    public static <T> List<List<T>> generateCombinations(List<T> items, int r) {
        List<List<T>> result = new ArrayList<>();
        generateCombinationsHelper(items, r, 0, new ArrayList<>(), result);
        return result;
    }

    private static <T> void generateCombinationsHelper(List<T> items, int r, int start, 
                                                      List<T> current, List<List<T>> result) {
        if (current.size() == r) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < items.size(); i++) {
            current.add(items.get(i));
            generateCombinationsHelper(items, r, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Calculates binomial coefficients for Pascal's triangle.
     */
    public static String generatePascalsTriangle(int rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pascal's Triangle (").append(rows).append(" rows)\n");
        sb.append("=".repeat(30)).append("\n\n");

        for (int i = 0; i < rows; i++) {
            // Add spacing for triangle shape
            for (int j = 0; j < rows - i - 1; j++) {
                sb.append("  ");
            }

            for (int j = 0; j <= i; j++) {
                BigInteger value = combinations(i, j);
                sb.append(String.format("%4s", value.toString()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Demonstrates the pigeonhole principle.
     */
    public static String demonstratePigeonholePrinciple() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pigeonhole Principle Demonstration\n");
        sb.append("=================================\n\n");

        sb.append("The Pigeonhole Principle states:\n");
        sb.append("If n pigeons are put into m pigeonholes with n > m,\n");
        sb.append("then at least one pigeonhole must contain more than one pigeon.\n\n");

        sb.append("Examples:\n");
        sb.append("1. In any group of 13 people, at least 2 share the same birthday month.\n");
        sb.append("   (13 people, 12 months â†’ 13 > 12)\n\n");

        sb.append("2. In any group of 367 people, at least 2 share the same birthday.\n");
        sb.append("   (367 people, 366 possible birthdays including Feb 29 â†’ 367 > 366)\n\n");

        sb.append("3. Among any 6 people, either 3 are mutual friends or 3 are mutual strangers.\n");
        sb.append("   (This is a more complex application involving graph coloring)\n\n");

        return sb.toString();
    }

    /**
     * Calculates Stirling numbers of the second kind.
     */
    public static BigInteger stirlingSecondKind(int n, int k) {
        if (n == 0 && k == 0) return BigInteger.ONE;
        if (n == 0 || k == 0) return BigInteger.ZERO;
        if (k > n) return BigInteger.ZERO;
        if (k == 1 || k == n) return BigInteger.ONE;

        // Use recurrence relation: S(n,k) = k*S(n-1,k) + S(n-1,k-1)
        BigInteger[][] dp = new BigInteger[n + 1][k + 1];

        // Base cases
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = BigInteger.ZERO;
            }
        }
        dp[0][0] = BigInteger.ONE;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.min(i, k); j++) {
                if (j == 1 || j == i) {
                    dp[i][j] = BigInteger.ONE;
                } else {
                    dp[i][j] = dp[i-1][j-1].add(dp[i-1][j].multiply(BigInteger.valueOf(j)));
                }
            }
        }

        return dp[n][k];
    }

    /**
     * Demonstrates derangements (permutations with no fixed points).
     */
    public static String demonstrateDerangements(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append("Derangements Demonstration\n");
        sb.append("=========================\n\n");

        sb.append("A derangement is a permutation where no element appears in its original position.\n\n");

        // Calculate derangements using the formula: D(n) = n! * Î£(k=0 to n) (-1)^k / k!
        BigInteger derangements = calculateDerangements(n);
        BigInteger totalPermutations = factorial(n);

        sb.append("For n = ").append(n).append(":\n");
        sb.append("Total permutations: ").append(totalPermutations).append("\n");
        sb.append("Derangements: ").append(derangements).append("\n");
        sb.append("Probability of derangement: ").append(derangements).append("/").append(totalPermutations);

        if (n <= 10) {
            double probability = derangements.doubleValue() / totalPermutations.doubleValue();
            sb.append(" â‰ˆ ").append(String.format("%.6f", probability));
            sb.append(" â‰ˆ 1/e â‰ˆ 0.367879");
        }
        sb.append("\n\n");

        return sb.toString();
    }

    private static BigInteger calculateDerangements(int n) {
        if (n == 0) return BigInteger.ONE;
        if (n == 1) return BigInteger.ZERO;

        // Use recurrence: D(n) = (n-1) * (D(n-1) + D(n-2))
        BigInteger[] dp = new BigInteger[n + 1];
        dp[0] = BigInteger.ONE;
        dp[1] = BigInteger.ZERO;

        for (int i = 2; i <= n; i++) {
            dp[i] = BigInteger.valueOf(i - 1).multiply(dp[i - 1].add(dp[i - 2]));
        }

        return dp[n];
    }

    /**
     * Creates a comprehensive combinatorics reference table.
     */
    public static String createCombinatoricsTable(int maxN) {
        StringBuilder sb = new StringBuilder();
        sb.append("Combinatorics Reference Table\n");
        sb.append("============================\n\n");

        sb.append("n | n! | P(n,2) | C(n,2) | D(n)\n");
        sb.append("--|----|---------|---------|---------\n");

        for (int n = 0; n <= Math.min(maxN, 10); n++) {
            BigInteger fact = factorial(n);
            BigInteger perm = n >= 2 ? permutations(n, 2) : BigInteger.ZERO;
            BigInteger comb = n >= 2 ? combinations(n, 2) : BigInteger.ZERO;
            BigInteger derang = calculateDerangements(n);

            sb.append(String.format("%2d|%4s|%9s|%9s|%9s\n", 
                n, fact, perm, comb, derang));
        }

        sb.append("\nLegend:\n");
        sb.append("n!     = factorial of n\n");
        sb.append("P(n,2) = permutations of n taken 2 at a time\n");
        sb.append("C(n,2) = combinations of n taken 2 at a time\n");
        sb.append("D(n)   = derangements of n elements\n");

        return sb.toString();
    }
}