package com.discretelogic.util;

import java.math.BigInteger;
import java.util.*;

/**
 * Mathematical utility functions for discrete mathematics operations.
 */
public class MathUtils {

    /**
     * Calculates the greatest common divisor using Euclidean algorithm.
     */
    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    /**
     * Calculates the least common multiple.
     */
    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return Math.abs(a * b) / gcd(a, b);
    }

    /**
     * Checks if a number is prime.
     */
    public static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Generates prime numbers up to n using Sieve of Eratosthenes.
     */
    public static List<Integer> generatePrimes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, 2, n + 1, true);

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                primes.add(i);
            }
        }

        return primes;
    }

    /**
     * Calculates modular exponentiation: (base^exp) mod mod.
     */
    public static long modularExponentiation(long base, long exp, long mod) {
        if (mod == 1) return 0;

        long result = 1;
        base = base % mod;

        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % mod;
            }
            exp = exp >> 1;
            base = (base * base) % mod;
        }

        return result;
    }

    /**
     * Calculates Fibonacci numbers up to n.
     */
    public static List<BigInteger> fibonacci(int n) {
        List<BigInteger> fib = new ArrayList<>();
        if (n <= 0) return fib;

        fib.add(BigInteger.ZERO);
        if (n > 1) {
            fib.add(BigInteger.ONE);

            for (int i = 2; i < n; i++) {
                BigInteger next = fib.get(i - 1).add(fib.get(i - 2));
                fib.add(next);
            }
        }

        return fib;
    }

    /**
     * Calculates the nth Fibonacci number efficiently.
     */
    public static BigInteger fibonacciNth(int n) {
        if (n <= 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        BigInteger a = BigInteger.ZERO;
        BigInteger b = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            BigInteger temp = a.add(b);
            a = b;
            b = temp;
        }

        return b;
    }

    /**
     * Converts a number to its binary representation with specified width.
     */
    public static String toBinaryString(long number, int width) {
        String binary = Long.toBinaryString(number);
        if (binary.length() > width) {
            return binary.substring(binary.length() - width);
        }
        return String.format("%" + width + "s", binary).replace(' ', '0');
    }

    /**
     * Counts the number of set bits (1s) in a number.
     */
    public static int countSetBits(long number) {
        int count = 0;
        while (number != 0) {
            count += number & 1;
            number >>>= 1;
        }
        return count;
    }

    /**
     * Generates all subsets of a given size from a list.
     */
    public static <T> List<List<T>> generateSubsets(List<T> items, int size) {
        List<List<T>> result = new ArrayList<>();
        generateSubsetsHelper(items, size, 0, new ArrayList<>(), result);
        return result;
    }

    private static <T> void generateSubsetsHelper(List<T> items, int size, int start, 
                                                 List<T> current, List<List<T>> result) {
        if (current.size() == size) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < items.size(); i++) {
            current.add(items.get(i));
            generateSubsetsHelper(items, size, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    /**
     * Calculates the binomial coefficient C(n, k).
     */
    public static BigInteger binomialCoefficient(int n, int k) {
        if (k > n || k < 0) return BigInteger.ZERO;
        if (k == 0 || k == n) return BigInteger.ONE;
        if (k > n - k) k = n - k; // Take advantage of symmetry

        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
            result = result.divide(BigInteger.valueOf(i + 1));
        }

        return result;
    }

    /**
     * Checks if a number is a power of 2.
     */
    public static boolean isPowerOfTwo(long n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * Calculates the floor of log base 2 of n.
     */
    public static int log2Floor(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Logarithm undefined for non-positive numbers");
        }
        return 63 - Long.numberOfLeadingZeros(n);
    }

    /**
     * Calculates the ceiling of log base 2 of n.
     */
    public static int log2Ceiling(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Logarithm undefined for non-positive numbers");
        }
        return isPowerOfTwo(n) ? log2Floor(n) : log2Floor(n) + 1;
    }

    /**
     * Generates a formatted mathematical report.
     */
    public static String generateMathReport(long number) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mathematical Analysis of ").append(number).append("\n");
        sb.append("=".repeat(30)).append("\n\n");

        sb.append("Basic Properties:\n");
        sb.append("- Even: ").append(number % 2 == 0 ? "Yes" : "No").append("\n");
        sb.append("- Prime: ").append(isPrime(Math.abs(number)) ? "Yes" : "No").append("\n");
        sb.append("- Power of 2: ").append(isPowerOfTwo(Math.abs(number)) ? "Yes" : "No").append("\n");

        if (number > 0) {
            sb.append("- Binary: ").append(Long.toBinaryString(number)).append("\n");
            sb.append("- Set bits: ").append(countSetBits(number)).append("\n");
            sb.append("- Logâ‚‚(floor): ").append(log2Floor(number)).append("\n");
            sb.append("- Logâ‚‚(ceiling): ").append(log2Ceiling(number)).append("\n");
        }

        return sb.toString();
    }
}