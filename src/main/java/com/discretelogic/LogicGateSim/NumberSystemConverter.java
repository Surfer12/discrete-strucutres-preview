package com.discretelogic.LogicGateSim;

import java.math.BigInteger;
import java.util.*;

/**
 * Converts numbers between different number systems (binary, octal, decimal, hexadecimal).
 */
public class NumberSystemConverter {

    /**
     * Converts a decimal number to binary.
     */
    public static String decimalToBinary(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder binary = new StringBuilder();
        long num = Math.abs(decimal);

        while (num > 0) {
            binary.insert(0, num % 2);
            num /= 2;
        }

        return decimal < 0 ? "-" + binary.toString() : binary.toString();
    }

    /**
     * Converts a binary string to decimal.
     */
    public static long binaryToDecimal(String binary) {
        binary = binary.trim();
        boolean negative = binary.startsWith("-");
        if (negative) binary = binary.substring(1);

        long decimal = 0;
        int power = 0;

        for (int i = binary.length() - 1; i >= 0; i--) {
            char bit = binary.charAt(i);
            if (bit == '1') {
                decimal += Math.pow(2, power);
            } else if (bit != '0') {
                throw new IllegalArgumentException("Invalid binary digit: " + bit);
            }
            power++;
        }

        return negative ? -decimal : decimal;
    }

    /**
     * Converts a decimal number to hexadecimal.
     */
    public static String decimalToHexadecimal(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder hex = new StringBuilder();
        long num = Math.abs(decimal);
        String hexDigits = "0123456789ABCDEF";

        while (num > 0) {
            hex.insert(0, hexDigits.charAt((int)(num % 16)));
            num /= 16;
        }

        return decimal < 0 ? "-" + hex.toString() : hex.toString();
    }

    /**
     * Converts a hexadecimal string to decimal.
     */
    public static long hexadecimalToDecimal(String hex) {
        hex = hex.trim().toUpperCase();
        boolean negative = hex.startsWith("-");
        if (negative) hex = hex.substring(1);

        long decimal = 0;
        int power = 0;

        for (int i = hex.length() - 1; i >= 0; i--) {
            char digit = hex.charAt(i);
            int value;

            if (digit >= '0' && digit <= '9') {
                value = digit - '0';
            } else if (digit >= 'A' && digit <= 'F') {
                value = digit - 'A' + 10;
            } else {
                throw new IllegalArgumentException("Invalid hexadecimal digit: " + digit);
            }

            decimal += value * Math.pow(16, power);
            power++;
        }

        return negative ? -decimal : decimal;
    }

    /**
     * Converts a decimal number to octal.
     */
    public static String decimalToOctal(long decimal) {
        if (decimal == 0) return "0";

        StringBuilder octal = new StringBuilder();
        long num = Math.abs(decimal);

        while (num > 0) {
            octal.insert(0, num % 8);
            num /= 8;
        }

        return decimal < 0 ? "-" + octal.toString() : octal.toString();
    }

    /**
     * Converts an octal string to decimal.
     */
    public static long octalToDecimal(String octal) {
        octal = octal.trim();
        boolean negative = octal.startsWith("-");
        if (negative) octal = octal.substring(1);

        long decimal = 0;
        int power = 0;

        for (int i = octal.length() - 1; i >= 0; i--) {
            char digit = octal.charAt(i);
            if (digit >= '0' && digit <= '7') {
                decimal += (digit - '0') * Math.pow(8, power);
            } else {
                throw new IllegalArgumentException("Invalid octal digit: " + digit);
            }
            power++;
        }

        return negative ? -decimal : decimal;
    }

    /**
     * Demonstrates all conversions for a given decimal number.
     */
    public static String demonstrateConversions(long decimal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Number System Conversions\n");
        sb.append("========================\n\n");

        sb.append("Original decimal number: ").append(decimal).append("\n\n");

        String binary = decimalToBinary(decimal);
        String octal = decimalToOctal(decimal);
        String hex = decimalToHexadecimal(decimal);

        sb.append("Conversions:\n");
        sb.append("Decimal:     ").append(decimal).append("\n");
        sb.append("Binary:      ").append(binary).append("\n");
        sb.append("Octal:       ").append(octal).append("\n");
        sb.append("Hexadecimal: ").append(hex).append("\n\n");

        // Verify conversions by converting back
        sb.append("Verification (converting back to decimal):\n");
        sb.append("From binary:      ").append(binaryToDecimal(binary)).append("\n");
        sb.append("From octal:       ").append(octalToDecimal(octal)).append("\n");
        sb.append("From hexadecimal: ").append(hexadecimalToDecimal(hex)).append("\n\n");

        return sb.toString();
    }

    /**
     * Performs binary arithmetic operations.
     */
    public static String performBinaryArithmetic(String bin1, String bin2, String operation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Binary Arithmetic\n");
        sb.append("=================\n\n");

        long dec1 = binaryToDecimal(bin1);
        long dec2 = binaryToDecimal(bin2);
        long result;

        sb.append("Operand 1: ").append(bin1).append(" (").append(dec1).append(" in decimal)\n");
        sb.append("Operand 2: ").append(bin2).append(" (").append(dec2).append(" in decimal)\n");
        sb.append("Operation: ").append(operation).append("\n\n");

        switch (operation.toLowerCase()) {
            case "add":
            case "+":
                result = dec1 + dec2;
                sb.append("Result: ").append(decimalToBinary(result)).append(" (").append(result).append(" in decimal)\n");
                break;
            case "subtract":
            case "-":
                result = dec1 - dec2;
                sb.append("Result: ").append(decimalToBinary(result)).append(" (").append(result).append(" in decimal)\n");
                break;
            case "multiply":
            case "*":
                result = dec1 * dec2;
                sb.append("Result: ").append(decimalToBinary(result)).append(" (").append(result).append(" in decimal)\n");
                break;
            case "and":
            case "&":
                result = dec1 & dec2;
                sb.append("Bitwise AND: ").append(decimalToBinary(result)).append(" (").append(result).append(" in decimal)\n");
                break;
            case "or":
            case "|":
                result = dec1 | dec2;
                sb.append("Bitwise OR: ").append(decimalToBinary(result)).append(" (").append(result).append(" in decimal)\n");
                break;
            case "xor":
            case "^":
                result = dec1 ^ dec2;
                sb.append("Bitwise XOR: ").append(decimalToBinary(result)).append(" (").append(result).append(" in decimal)\n");
                break;
            default:
                sb.append("Unknown operation: ").append(operation).append("\n");
        }

        return sb.toString();
    }

    /**
     * Shows step-by-step conversion from decimal to binary.
     */
    public static String showDecimalToBinarySteps(long decimal) {
        StringBuilder sb = new StringBuilder();
        sb.append("Step-by-step Decimal to Binary Conversion\n");
        sb.append("========================================\n\n");

        if (decimal == 0) {
            sb.append("Decimal: 0\n");
            sb.append("Binary:  0\n");
            return sb.toString();
        }

        sb.append("Converting decimal ").append(decimal).append(" to binary:\n\n");

        long num = Math.abs(decimal);
        List<String> steps = new ArrayList<>();

        while (num > 0) {
            long quotient = num / 2;
            long remainder = num % 2;
            steps.add(num + " Ã· 2 = " + quotient + " remainder " + remainder);
            num = quotient;
        }

        sb.append("Division steps (read remainders from bottom to top):\n");
        for (String step : steps) {
            sb.append(step).append("\n");
        }

        sb.append("\nBinary result: ");
        if (decimal < 0) sb.append("-");
        for (int i = steps.size() - 1; i >= 0; i--) {
            String step = steps.get(i);
            sb.append(step.substring(step.length() - 1));
        }
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Creates a conversion table for numbers 0-15.
     */
    public static String createConversionTable() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number System Conversion Table (0-15)\n");
        sb.append("====================================\n\n");

        sb.append("Dec | Bin  | Oct | Hex\n");
        sb.append("----|------|-----|----\n");

        for (int i = 0; i <= 15; i++) {
            sb.append(String.format("%3d | %4s | %3s | %3s\n",
                i,
                decimalToBinary(i),
                decimalToOctal(i),
                decimalToHexadecimal(i)
            ));
        }

        return sb.toString();
    }
}