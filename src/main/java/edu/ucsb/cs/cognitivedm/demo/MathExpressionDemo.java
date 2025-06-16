package edu.ucsb.cs.cognitivedm.demo;

import edu.ucsb.cs.cognitivedm.MathExpression;
import edu.ucsb.cs.cognitivedm.framework.AttentionRecognitionFramework;
import edu.ucsb.cs.cognitivedm.framework.CognitiveState;
import edu.ucsb.cs.cognitivedm.education.LearningContent;
import edu.ucsb.cs.cognitivedm.education.ContentType;
import edu.ucsb.cs.cognitivedm.education.DifficultyLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates the usage of MathExpression class with various cognitive processing features.
 */
public class MathExpressionDemo {
    private static final String SEPARATOR = "=".repeat(80);
    private static final String SUBSEPARATOR = "-".repeat(50);

    public static void main(String[] args) {
        MathExpressionDemo demo = new MathExpressionDemo();
        demo.demonstrateBasicSetTheoryProcessing();
        demo.demonstratePsiOptimization();
        demo.demonstrateLexicalNetworkIntegration();
        demo.demonstrateMetaController();
        demo.demonstrateEducationalAdaptivity();
        demo.demonstrateAttentionRestoration();
        demo.demonstrateCompleteIntegration();
    }

    public void demonstrateBasicSetTheoryProcessing() {
        System.out.println(SEPARATOR);
        System.out.println("Basic Set Theory Processing Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        System.out.println(SUBSEPARATOR);
    }

    public void demonstratePsiOptimization() {
        System.out.println(SEPARATOR);
        System.out.println("Ψ(x) Optimization Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        // Perform Ψ(x) optimization
        MathExpression.PsiOptimizationResult result = mathExpr.optimizePsi(expr);
        System.out.printf("  Optimized Expression: %s%n", result.getOptimizedExpression());
        System.out.printf("  Optimization Score: %.3f%n", result.getOptimizationScore());

        System.out.println(SUBSEPARATOR);
    }

    public void demonstrateLexicalNetworkIntegration() {
        System.out.println(SEPARATOR);
        System.out.println("Lexical Network Integration Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        // Get notation suggestions
        List<String> suggestions = mathExpr.getNotationSuggestions(expr);
        System.out.println("  Notation Suggestions: " + suggestions);

        System.out.println(SUBSEPARATOR);
    }

    public void demonstrateMetaController() {
        System.out.println(SEPARATOR);
        System.out.println("Meta-Controller Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        // Perform meta-controller analysis
        MathExpression.MetaAnalysis metaAnalysis = mathExpr.analyzeMetaController(expr);
        System.out.printf("  Attention Drift: %.3f%n", metaAnalysis.getAttentionDrift());
        System.out.printf("  System Health: %.3f%n", metaAnalysis.getSystemHealth());

        System.out.println(SUBSEPARATOR);
    }

    public void demonstrateEducationalAdaptivity() {
        System.out.println(SEPARATOR);
        System.out.println("Educational Adaptivity Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        // Perform educational adaptivity
        String optimizedExpr = mathExpr.optimizeExpression(expr);
        double optimizationScore = mathExpr.getOptimizationScore(expr);
        System.out.printf("  Optimized Expression: %s%n", optimizedExpr);
        System.out.printf("  Optimization Score: %.3f%n", optimizationScore);

        System.out.println(SUBSEPARATOR);
    }

    public void demonstrateAttentionRestoration() {
        System.out.println(SEPARATOR);
        System.out.println("Attention Restoration Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        // Perform attention restoration
        mathExpr.restoreAttention();
        System.out.println("  Attention restored successfully");

        System.out.println(SUBSEPARATOR);
    }

    public void demonstrateCompleteIntegration() {
        System.out.println(SEPARATOR);
        System.out.println("Complete Integration Demo");
        System.out.println(SEPARATOR);

        MathExpression mathExpr = new MathExpression();
        String expr = "A ∪ B ∩ C";

        // Show lexical viability
        double viability = mathExpr.calculateLexicalViability(expr, new ArrayList<>());
        System.out.printf("  Lexical Viability: %.3f%n", viability);

        // Show notation preference
        double preference = mathExpr.getNotationPreference(expr);
        System.out.printf("  Notation Preference: %.3f%n", preference);

        // Update learner profile
        Map<String, Double> profile = new HashMap<>();
        profile.put("set_theory_familiarity", 0.8);
        profile.put("symbol_recognition", 0.7);

        for (Map.Entry<String, Double> profileEntry : profile.entrySet()) {
            mathExpr.updateLearnerProfile(profileEntry.getKey(), profileEntry.getValue());
        }

        // Perform complete integration
        String optimizedExpr = mathExpr.optimizeExpression(expr);
        double optimizationScore = mathExpr.getOptimizationScore(expr);
        System.out.printf("  Optimized Expression: %s%n", optimizedExpr);
        System.out.printf("  Optimization Score: %.3f%n", optimizationScore);

        System.out.println(SUBSEPARATOR);
    }
}
