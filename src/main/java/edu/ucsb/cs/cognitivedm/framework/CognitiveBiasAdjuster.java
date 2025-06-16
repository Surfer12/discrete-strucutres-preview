package edu.ucsb.cs.cognitivedm.framework;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CognitiveBiasAdjuster for modeling and adjusting human-like biases
 *
 * Implements the P(H|E,β) component of the cognitive optimization equation,
 * modeling various cognitive biases that affect human reasoning and decision-making.
 * This component allows AI systems to understand and adapt to human cognitive patterns.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class CognitiveBiasAdjuster {
    private final Map<String, Double> biasParameters;
    private final Map<String, BiasModel> biasModels;
    private final Random random;
    private double globalBiasStrength;

    /**
     * Represents a specific cognitive bias model
     */
    public static class BiasModel {
        private final String name;
        private final String description;
        private final BiasType type;
        private double strength;
        private final Map<String, Double> parameters;

        public BiasModel(String name, String description, BiasType type, double strength) {
            this.name = name;
            this.description = description;
            this.type = type;
            this.strength = Math.max(0.0, Math.min(2.0, strength));
            this.parameters = new HashMap<>();
        }

        public void setParameter(String key, double value) {
            parameters.put(key, value);
        }

        public double getParameter(String key, double defaultValue) {
            return parameters.getOrDefault(key, defaultValue);
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BiasType getType() { return type; }
        public double getStrength() { return strength; }
        public void setStrength(double strength) { this.strength = Math.max(0.0, Math.min(2.0, strength)); }
        public Map<String, Double> getParameters() { return new HashMap<>(parameters); }
    }

    /**
     * Types of cognitive biases
     */
    public enum BiasType {
        CONFIRMATION,     // Seeking confirming evidence
        AVAILABILITY,     // Overweighting easily recalled information
        ANCHORING,        // Over-reliance on first information
        REPRESENTATIVENESS, // Judging by similarity to prototypes
        OVERCONFIDENCE,   // Overestimating accuracy of beliefs
        FRAMING,          // Different decisions based on presentation
        LOSS_AVERSION,    // Preferring avoiding losses to acquiring gains
        HINDSIGHT,        // "I knew it all along" effect
        SUNK_COST,        // Continuing due to previously invested resources
        PRIMING           // Influence of recent exposure
    }

    /**
     * Constructor
     */
    public CognitiveBiasAdjuster() {
        this.biasParameters = new ConcurrentHashMap<>();
        this.biasModels = new ConcurrentHashMap<>();
        this.random = new Random();
        this.globalBiasStrength = 1.0;
        initializeBiasModels();
    }

    /**
     * Initialize standard cognitive bias models
     */
    private void initializeBiasModels() {
        // Confirmation Bias
        BiasModel confirmationBias = new BiasModel(
            "confirmation_bias",
            "Tendency to search for, interpret, and recall information in a way that confirms pre-existing beliefs",
            BiasType.CONFIRMATION,
            1.2
        );
        confirmationBias.setParameter("confirmation_weight", 0.3);
        biasModels.put("confirmation_bias", confirmationBias);

        // Availability Heuristic
        BiasModel availabilityBias = new BiasModel(
            "availability_heuristic",
            "Estimating probability based on how easily examples come to mind",
            BiasType.AVAILABILITY,
            1.1
        );
        availabilityBias.setParameter("recency_weight", 0.4);
        availabilityBias.setParameter("vividness_multiplier", 1.5);
        biasModels.put("availability_heuristic", availabilityBias);

        // Anchoring Bias
        BiasModel anchoringBias = new BiasModel(
            "anchoring_bias",
            "Heavy reliance on first piece of information encountered",
            BiasType.ANCHORING,
            1.3
        );
        anchoringBias.setParameter("anchor_strength", 0.5);
        anchoringBias.setParameter("adjustment_factor", 0.7);
        biasModels.put("anchoring_bias", anchoringBias);

        // Representativeness Heuristic
        BiasModel representativenessBias = new BiasModel(
            "representativeness_heuristic",
            "Judging probability by similarity to mental prototypes",
            BiasType.REPRESENTATIVENESS,
            1.1
        );
        representativenessBias.setParameter("prototype_weight", 0.6);
        biasModels.put("representativeness_heuristic", representativenessBias);

        // Overconfidence Bias
        BiasModel overconfidenceBias = new BiasModel(
            "overconfidence_bias",
            "Tendency to overestimate accuracy of one's beliefs and judgments",
            BiasType.OVERCONFIDENCE,
            1.4
        );
        overconfidenceBias.setParameter("confidence_multiplier", 1.3);
        biasModels.put("overconfidence_bias", overconfidenceBias);

        // Framing Effect
        BiasModel framingBias = new BiasModel(
            "framing_effect",
            "Different choices based on how options are presented",
            BiasType.FRAMING,
            1.2
        );
        framingBias.setParameter("positive_frame_boost", 0.2);
        framingBias.setParameter("negative_frame_penalty", -0.3);
        biasModels.put("framing_effect", framingBias);

        // Loss Aversion
        BiasModel lossAversionBias = new BiasModel(
            "loss_aversion",
            "Preferring to avoid losses rather than acquire equivalent gains",
            BiasType.LOSS_AVERSION,
            1.5
        );
        lossAversionBias.setParameter("loss_weight_multiplier", 2.0);
        biasModels.put("loss_aversion", lossAversionBias);
    }

    /**
     * Calculate biased probability P(H|E,β)
     * @param baseProb Base probability P(H|E)
     * @param evidence Evidence information
     * @param context Contextual information
     * @return Bias-adjusted probability
     */
    public double calculateBiasedProbability(double baseProb, Map<String, Object> evidence,
                                           Map<String, Object> context) {
        if (baseProb < 0.0 || baseProb > 1.0) {
            throw new IllegalArgumentException("Base probability must be between 0 and 1");
        }

        double adjustedProb = baseProb;

        // Apply each active bias model
        for (BiasModel model : biasModels.values()) {
            if (model.getStrength() > 0.1) { // Only apply if bias is significant
                adjustedProb = applyBiasModel(adjustedProb, model, evidence, context);
            }
        }

        // Apply global bias strength adjustment
        adjustedProb = applyGlobalBiasAdjustment(baseProb, adjustedProb);

        // Ensure result stays within valid probability range
        return Math.max(0.0, Math.min(1.0, adjustedProb));
    }

    /**
     * Apply a specific bias model to adjust probability
     */
    private double applyBiasModel(double currentProb, BiasModel model,
                                 Map<String, Object> evidence, Map<String, Object> context) {
        switch (model.getType()) {
            case CONFIRMATION:
                return applyConfirmationBias(currentProb, model, evidence, context);
            case AVAILABILITY:
                return applyAvailabilityBias(currentProb, model, evidence, context);
            case ANCHORING:
                return applyAnchoringBias(currentProb, model, evidence, context);
            case REPRESENTATIVENESS:
                return applyRepresentativenessBias(currentProb, model, evidence, context);
            case OVERCONFIDENCE:
                return applyOverconfidenceBias(currentProb, model, evidence, context);
            case FRAMING:
                return applyFramingBias(currentProb, model, evidence, context);
            case LOSS_AVERSION:
                return applyLossAversionBias(currentProb, model, evidence, context);
            default:
                return currentProb;
        }
    }

    /**
     * Apply confirmation bias
     */
    private double applyConfirmationBias(double prob, BiasModel model,
                                       Map<String, Object> evidence, Map<String, Object> context) {
        // Confirmation bias increases probability if evidence seems to confirm expectations
        Boolean confirmsExpectation = (Boolean) context.get("confirms_expectation");
        if (confirmsExpectation != null && confirmsExpectation) {
            double confirmationWeight = model.getParameter("confirmation_weight", 0.3);
            double adjustment = confirmationWeight * model.getStrength();
            return prob + (1.0 - prob) * adjustment;
        } else if (confirmsExpectation != null && !confirmsExpectation) {
            // Reduce probability if evidence contradicts expectations
            double confirmationWeight = model.getParameter("confirmation_weight", 0.3);
            double adjustment = confirmationWeight * model.getStrength() * 0.5;
            return prob - prob * adjustment;
        }
        return prob;
    }

    /**
     * Apply availability heuristic bias
     */
    private double applyAvailabilityBias(double prob, BiasModel model,
                                       Map<String, Object> evidence, Map<String, Object> context) {
        // Availability bias increases probability for easily recalled events
        Double recency = (Double) context.get("recency_score");
        Double vividness = (Double) context.get("vividness_score");

        if (recency != null || vividness != null) {
            double recencyWeight = model.getParameter("recency_weight", 0.4);
            double vividnessMultiplier = model.getParameter("vividness_multiplier", 1.5);

            double recencyAdjustment = recency != null ? recency * recencyWeight : 0.0;
            double vividnessAdjustment = vividness != null ? vividness * (vividnessMultiplier - 1.0) : 0.0;

            double totalAdjustment = (recencyAdjustment + vividnessAdjustment) * model.getStrength();
            return prob + (1.0 - prob) * totalAdjustment * 0.3;
        }
        return prob;
    }

    /**
     * Apply anchoring bias
     */
    private double applyAnchoringBias(double prob, BiasModel model,
                                    Map<String, Object> evidence, Map<String, Object> context) {
        // Anchoring bias pulls probability toward initial anchor value
        Double anchorValue = (Double) context.get("anchor_value");
        if (anchorValue != null && anchorValue >= 0.0 && anchorValue <= 1.0) {
            double anchorStrength = model.getParameter("anchor_strength", 0.5);
            double adjustmentFactor = model.getParameter("adjustment_factor", 0.7);

            // Pull toward anchor, but allow some adjustment
            double anchorInfluence = anchorStrength * model.getStrength();
            double adjustedAnchor = anchorValue + (prob - anchorValue) * adjustmentFactor;

            return prob * (1.0 - anchorInfluence) + adjustedAnchor * anchorInfluence;
        }
        return prob;
    }

    /**
     * Apply representativeness heuristic bias
     */
    private double applyRepresentativenessBias(double prob, BiasModel model,
                                             Map<String, Object> evidence, Map<String, Object> context) {
        // Representativeness bias adjusts based on similarity to prototypes
        Double similarityScore = (Double) context.get("prototype_similarity");
        if (similarityScore != null) {
            double prototypeWeight = model.getParameter("prototype_weight", 0.6);
            double adjustment = (similarityScore - 0.5) * prototypeWeight * model.getStrength();
            return prob + adjustment * 0.4;
        }
        return prob;
    }

    /**
     * Apply overconfidence bias
     */
    private double applyOverconfidenceBias(double prob, BiasModel model,
                                         Map<String, Object> evidence, Map<String, Object> context) {
        // Overconfidence bias makes probabilities more extreme
        double confidenceMultiplier = model.getParameter("confidence_multiplier", 1.3);
        double biasStrength = model.getStrength();

        // Push probabilities away from 0.5 (more extreme)
        double deviation = prob - 0.5;
        double extremeDeviation = deviation * confidenceMultiplier * biasStrength;

        return 0.5 + extremeDeviation * 0.8; // Moderate the effect
    }

    /**
     * Apply framing effect bias
     */
    private double applyFramingBias(double prob, BiasModel model,
                                  Map<String, Object> evidence, Map<String, Object> context) {
        // Framing bias adjusts based on positive/negative presentation
        String frameType = (String) context.get("frame_type");
        if (frameType != null) {
            double adjustment = 0.0;
            if ("positive".equals(frameType)) {
                adjustment = model.getParameter("positive_frame_boost", 0.2);
            } else if ("negative".equals(frameType)) {
                adjustment = model.getParameter("negative_frame_penalty", -0.3);
            }

            return prob + adjustment * model.getStrength() * 0.3;
        }
        return prob;
    }

    /**
     * Apply loss aversion bias
     */
    private double applyLossAversionBias(double prob, BiasModel model,
                                       Map<String, Object> evidence, Map<String, Object> context) {
        // Loss aversion affects decisions involving potential losses
        Boolean involvesLoss = (Boolean) context.get("involves_loss");
        if (involvesLoss != null && involvesLoss) {
            double lossMultiplier = model.getParameter("loss_weight_multiplier", 2.0);
            // Reduce probability for options involving losses
            double lossAversion = (lossMultiplier - 1.0) * model.getStrength() * 0.2;
            return prob - prob * lossAversion;
        }
        return prob;
    }

    /**
     * Apply global bias strength adjustment
     */
    private double applyGlobalBiasAdjustment(double baseProb, double biasedProb) {
        // Blend between base and fully biased probability based on global bias strength
        return baseProb * (1.0 - globalBiasStrength) + biasedProb * globalBiasStrength;
    }

    /**
     * Set the strength of a specific bias
     */
    public void setBiasStrength(String biasName, double strength) {
        BiasModel model = biasModels.get(biasName);
        if (model != null) {
            model.setStrength(strength);
        }
    }

    /**
     * Get the strength of a specific bias
     */
    public double getBiasStrength(String biasName) {
        BiasModel model = biasModels.get(biasName);
        return model != null ? model.getStrength() : 0.0;
    }

    /**
     * Set global bias strength (affects all biases)
     */
    public void setGlobalBiasStrength(double strength) {
        this.globalBiasStrength = Math.max(0.0, Math.min(2.0, strength));
    }

    /**
     * Get global bias strength
     */
    public double getGlobalBiasStrength() {
        return globalBiasStrength;
    }

    /**
     * Add a custom bias model
     */
    public void addBiasModel(BiasModel model) {
        biasModels.put(model.getName(), model);
    }

    /**
     * Remove a bias model
     */
    public void removeBiasModel(String biasName) {
        biasModels.remove(biasName);
    }

    /**
     * Get all bias models
     */
    public Map<String, BiasModel> getBiasModels() {
        return new HashMap<>(biasModels);
    }

    /**
     * Get active biases (strength > 0.1)
     */
    public List<BiasModel> getActiveBiases() {
        return biasModels.values().stream()
            .filter(model -> model.getStrength() > 0.1)
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Reset all biases to default values
     */
    public void resetToDefaults() {
        biasModels.clear();
        globalBiasStrength = 1.0;
        initializeBiasModels();
    }

    /**
     * Create a cognitive profile with specific bias patterns
     */
    public void applyCognitiveProfile(String profileName) {
        switch (profileName.toLowerCase()) {
            case "conservative":
                setBiasStrength("confirmation_bias", 1.5);
                setBiasStrength("loss_aversion", 1.8);
                setBiasStrength("anchoring_bias", 1.4);
                setGlobalBiasStrength(1.2);
                break;
            case "optimistic":
                setBiasStrength("overconfidence_bias", 1.6);
                setBiasStrength("availability_heuristic", 1.3);
                setBiasStrength("confirmation_bias", 0.8);
                setGlobalBiasStrength(1.1);
                break;
            case "analytical":
                setBiasStrength("confirmation_bias", 0.7);
                setBiasStrength("overconfidence_bias", 0.8);
                setBiasStrength("representativeness_heuristic", 0.6);
                setGlobalBiasStrength(0.8);
                break;
            case "intuitive":
                setBiasStrength("availability_heuristic", 1.4);
                setBiasStrength("representativeness_heuristic", 1.5);
                setBiasStrength("framing_effect", 1.3);
                setGlobalBiasStrength(1.3);
                break;
            default:
                resetToDefaults();
        }
    }

    /**
     * Calculate overall bias intensity
     */
    public double calculateOverallBiasIntensity() {
        double totalBias = biasModels.values().stream()
            .mapToDouble(BiasModel::getStrength)
            .average()
            .orElse(1.0);

        return totalBias * globalBiasStrength;
    }

    /**
     * Generate bias report
     */
    public String generateBiasReport() {
        StringBuilder report = new StringBuilder();
        report.append("Cognitive Bias Profile Report\n");
        report.append("============================\n");
        report.append(String.format("Global Bias Strength: %.2f\n", globalBiasStrength));
        report.append(String.format("Overall Bias Intensity: %.2f\n", calculateOverallBiasIntensity()));
        report.append("\nActive Biases:\n");

        getActiveBiases().forEach(bias -> {
            report.append(String.format("- %s: %.2f (%s)\n",
                bias.getName(), bias.getStrength(), bias.getDescription()));
        });

        return report.toString();
    }
}
