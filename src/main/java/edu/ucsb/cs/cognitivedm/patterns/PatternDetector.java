package edu.ucsb.cs.cognitivedm.patterns;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.discretelogic.util.LoggingUtil;
import edu.ucsb.cs.cognitivedm.framework.CognitiveFrameworkTypes.Pattern;
import org.slf4j.Logger;

/**
 * PatternDetector for cognitive-inspired discrete mathematics
 *
 * Implements fractal pattern analysis with Hurst exponent calculation,
 * self-similarity detection, and recursive pattern recognition based on
 * the attention-recognition decoupling framework.
 *
 * @author UCSB Cognitive Discrete Mathematics Team
 * @version 1.0
 */
public class PatternDetector {
    private static final Logger LOGGER = LoggingUtil.getLogger(PatternDetector.class);

    private final int scale;
    private final double hurstThreshold;
    private final double selfSimilarityThreshold;
    private final int minDataPoints;

    /**
     * Represents a detected pattern with cognitive metrics
     */
    public static class Pattern {
        private final String type;
        private final String description;
        private final int scale;
        private final double confidence;
        private final long duration;
        private final Map<String, Double> characteristics;
        private final long timestamp;

        public Pattern(String type, String description, int scale, double confidence) {
            this(type, description, scale, confidence, 0L);
        }

        public Pattern(String type, String description, int scale, double confidence, long duration) {
            this.type = type;
            this.description = description;
            this.scale = scale;
            this.confidence = Math.max(0.0, Math.min(1.0, confidence));
            this.duration = duration;
            this.characteristics = new HashMap<>();
            this.timestamp = System.currentTimeMillis();
        }

        public void addCharacteristic(String key, double value) {
            characteristics.put(key, value);
        }

        // Getters
        public String getType() { return type; }
        public String getDescription() { return description; }
        public int getScale() { return scale; }
        public double getConfidence() { return confidence; }
        public long getDuration() { return duration; }
        public Map<String, Double> getCharacteristics() { return new HashMap<>(characteristics); }
        public long getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return String.format("Pattern{type='%s', confidence=%.3f, scale=%d, duration=%d}",
                               type, confidence, scale, duration);
        }
    }

    /**
     * Constructor
     * @param scale The temporal/spatial scale for pattern detection
     */
    public PatternDetector(int scale) {
        this.scale = scale;
        this.hurstThreshold = 0.5;
        this.selfSimilarityThreshold = 0.7;
        this.minDataPoints = 8;
    }

    /**
     * Analyze a sequence of multi-dimensional time series data for patterns
     * @param timeSeries List of data points, each represented as double array
     * @return List of detected patterns
     */
    public List<Pattern> analyzeSequence(List<double[]> timeSeries) {
        List<Pattern> patterns = new ArrayList<>();

        if (timeSeries == null || timeSeries.size() < minDataPoints) {
            return patterns;
        }

        // Basic pattern detection
        patterns.addAll(analyzeBasicPatterns(timeSeries));

        // Fractal pattern analysis
        patterns.addAll(analyzeFractalPatterns(timeSeries));

        // Recursive pattern detection
        patterns.addAll(analyzeRecursivePatterns(timeSeries));

        // Attention-wandering patterns
        patterns.addAll(analyzeAttentionPatterns(timeSeries));

        // Cross-scale pattern detection
        patterns.addAll(analyzeCrossScalePatterns(timeSeries));

        return patterns.stream()
            .sorted((p1, p2) -> Double.compare(p2.getConfidence(), p1.getConfidence()))
            .collect(Collectors.toList());
    }

    /**
     * Analyze basic statistical patterns
     */
    private List<Pattern> analyzeBasicPatterns(List<double[]> timeSeries) {
        List<Pattern> patterns = new ArrayList<>();

        if (timeSeries.isEmpty()) return patterns;

        int dimensions = timeSeries.get(0).length;

        for (int dim = 0; dim < dimensions; dim++) {
            double[] values = extractDimension(timeSeries, dim);

            // Trend analysis
            double trend = calculateTrend(values);
            if (Math.abs(trend) > 0.1) {
                Pattern trendPattern = new Pattern(
                    "Trend",
                    String.format("Dimension %d shows %s trend (slope=%.3f)",
                                dim, trend > 0 ? "upward" : "downward", trend),
                    scale,
                    Math.min(1.0, Math.abs(trend) * 2)
                );
                trendPattern.addCharacteristic("slope", trend);
                trendPattern.addCharacteristic("dimension", dim);
                patterns.add(trendPattern);
            }

            // Periodicity detection
            double periodicity = detectPeriodicity(values);
            if (periodicity > 0.6) {
                Pattern periodicPattern = new Pattern(
                    "Periodic",
                    String.format("Periodic pattern detected in dimension %d", dim),
                    scale,
                    periodicity
                );
                periodicPattern.addCharacteristic("periodicity_strength", periodicity);
                periodicPattern.addCharacteristic("dimension", dim);
                patterns.add(periodicPattern);
            }

            // Volatility analysis
            double volatility = calculateVolatility(values);
            if (volatility > 0.7) {
                Pattern volatilityPattern = new Pattern(
                    "HighVolatility",
                    String.format("High volatility detected in dimension %d", dim),
                    scale,
                    Math.min(1.0, volatility)
                );
                volatilityPattern.addCharacteristic("volatility", volatility);
                volatilityPattern.addCharacteristic("dimension", dim);
                patterns.add(volatilityPattern);
            }
        }

        return patterns;
    }

    /**
     * Analyze fractal patterns using Hurst exponent and self-similarity
     */
    public List<Pattern> analyzeFractalPatterns(List<double[]> timeSeries) {
        LOGGER.debug("Analyzing fractal patterns in time series with {} data points", timeSeries.size());
        
        List<Pattern> patterns = new ArrayList<>();

        if (timeSeries == null || timeSeries.size() < 10) {
            LOGGER.warn("Insufficient data points for pattern analysis");
            return patterns;
        }

        try {
            // Hurst exponent analysis
            double hurstExponent = calculateHurstExponent(timeSeries);
            double hurstThreshold = 0.6; // Configurable threshold

            // Ensure hurstExponent is a valid double
            if (!Double.isNaN(hurstExponent) && hurstExponent > hurstThreshold && hurstExponent < 1.0) {
                Pattern fractalPattern = new Pattern(
                    "FractalPersistence",
                    "Fractal persistence pattern with Hurst exponent " + hurstExponent,
                    calculatePatternConfidence(hurstExponent)
                );
                patterns.add(fractalPattern);
            }

            // Recursive self-similarity check
            double selfSimilarity = analyzeSelfSimilarity(timeSeries);
            if (selfSimilarity > selfSimilarityThreshold) {
                Pattern selfSimilarPattern = new Pattern(
                    "RecursiveSelfSimilarity",
                    "Self-similarity pattern with coefficient " + String.format("%.3f", selfSimilarity),
                    scale,
                    selfSimilarity
                );
                selfSimilarPattern.addCharacteristic("self_similarity", selfSimilarity);
                patterns.add(selfSimilarPattern);
            }

            // Multi-fractal analysis
            double multiFractalDimension = calculateMultiFractalDimension(timeSeries);
            if (multiFractalDimension > 1.5 && multiFractalDimension < 2.5) {
                Pattern multiFractalPattern = new Pattern(
                    "MultiFractal",
                    "Multi-fractal pattern with dimension " + multiFractalDimension,
                    scale,
                    Math.min(1.0, (multiFractalDimension - 1.0) / 1.5)
                );
                multiFractalPattern.addCharacteristic("fractal_dimension", multiFractalDimension);
                patterns.add(multiFractalPattern);
            }

            return patterns;
        } catch (Exception e) {
            LOGGER.error("Error analyzing fractal patterns", e);
            return patterns;
        }
    }

    /**
     * Calculate Hurst exponent using rescaled range analysis
     */
    private double calculateHurstExponent(List<double[]> series) {
        if (series.size() < 10) return 0.5;

        // Use first dimension for Hurst analysis
        double[] values = extractDimension(series, 0);
        int n = values.length;

        // Calculate mean-adjusted series
        double mean = Arrays.stream(values).average().orElse(0.0);
        double[] adjusted = Arrays.stream(values)
            .map(v -> v - mean)
            .toArray();

        // Calculate cumulative deviations
        double[] cumulative = new double[n];
        cumulative[0] = adjusted[0];
        for (int i = 1; i < n; i++) {
            cumulative[i] = cumulative[i-1] + adjusted[i];
        }

        // Calculate range and standard deviation
        double range = Arrays.stream(cumulative).max().orElse(0.0) -
                      Arrays.stream(cumulative).min().orElse(0.0);

        double variance = Arrays.stream(adjusted)
            .map(v -> v * v)
            .sum() / (n - 1);
        double stdDev = Math.sqrt(variance);

        // Hurst exponent approximation
        if (range == 0 || stdDev == 0) return 0.5;

        double rescaledRange = range / stdDev;
        return Math.log(rescaledRange) / Math.log(n);
    }

    /**
     * Analyze self-similarity across different scales
     */
    private double analyzeSelfSimilarity(List<double[]> series) {
        if (series.size() < 16) return 0.0;

        // Compare original with downsampled versions
        double[] original = extractDimension(series, 0);
        double[] downsampled2 = downsampleSeries(original, 2);
        double[] downsampled4 = downsampleSeries(original, 4);

        // Calculate correlations between scales
        double corr2 = calculateCorrelation(
            Arrays.copyOf(original, downsampled2.length),
            downsampled2
        );

        double corr4 = calculateCorrelation(
            Arrays.copyOf(original, downsampled4.length),
            downsampled4
        );

        // Average correlation as self-similarity measure
        return (corr2 + corr4) / 2.0;
    }

    /**
     * Calculate multi-fractal dimension using box-counting method
     */
    private double calculateMultiFractalDimension(List<double[]> series) {
        if (series.size() < 16) return 1.0;

        double[] values = extractDimension(series, 0);

        // Normalize values to [0,1] range
        double min = Arrays.stream(values).min().orElse(0.0);
        double max = Arrays.stream(values).max().orElse(1.0);
        double range = max - min;

        if (range == 0) return 1.0;

        double[] normalized = Arrays.stream(values)
            .map(v -> (v - min) / range)
            .toArray();

        // Simple box-counting approximation
        List<Double> boxSizes = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5);
        List<Integer> boxCounts = new ArrayList<>();

        for (double boxSize : boxSizes) {
            int count = countBoxes(normalized, boxSize);
            boxCounts.add(count);
        }

        // Calculate dimension from log-log slope
        return calculateLogLogSlope(boxSizes, boxCounts);
    }

    /**
     * Analyze recursive patterns in the data
     */
    private List<Pattern> analyzeRecursivePatterns(List<double[]> timeSeries) {
        List<Pattern> patterns = new ArrayList<>();

        if (timeSeries.size() < 12) return patterns;

        // Look for recursive structures: z = z² + c pattern
        for (int dim = 0; dim < timeSeries.get(0).length; dim++) {
            double[] values = extractDimension(timeSeries, dim);

            // Check for quadratic recurrence patterns
            double recursiveStrength = detectQuadraticRecurrence(values);
            if (recursiveStrength > 0.6) {
                Pattern recursivePattern = new Pattern(
                    "QuadraticRecurrence",
                    String.format("Quadratic recurrence pattern in dimension %d", dim),
                    scale,
                    recursiveStrength
                );
                recursivePattern.addCharacteristic("recursive_strength", recursiveStrength);
                recursivePattern.addCharacteristic("dimension", dim);
                patterns.add(recursivePattern);
            }

            // Detect feedback loops
            double feedbackStrength = detectFeedbackLoop(values);
            if (feedbackStrength > 0.5) {
                Pattern feedbackPattern = new Pattern(
                    "FeedbackLoop",
                    String.format("Feedback loop detected in dimension %d", dim),
                    scale,
                    feedbackStrength
                );
                feedbackPattern.addCharacteristic("feedback_strength", feedbackStrength);
                feedbackPattern.addCharacteristic("dimension", dim);
                patterns.add(feedbackPattern);
            }
        }

        return patterns;
    }

    /**
     * Analyze attention-specific patterns for cognitive modeling
     */
    private List<Pattern> analyzeAttentionPatterns(List<double[]> timeSeries) {
        List<Pattern> patterns = new ArrayList<>();

        if (timeSeries.size() < 8 || timeSeries.get(0).length < 3) return patterns;

        // Assuming dimensions are [attention, recognition, wandering]
        double[] attention = extractDimension(timeSeries, 0);
        double[] recognition = extractDimension(timeSeries, 1);
        double[] wandering = extractDimension(timeSeries, 2);

        // Attention-recognition coupling analysis
        double coupling = calculateCorrelation(attention, recognition);
        if (Math.abs(coupling) > 0.7) {
            Pattern couplingPattern = new Pattern(
                coupling > 0 ? "AttentionRecognitionCoupling" : "AttentionRecognitionDecoupling",
                String.format("Attention-recognition %s detected (r=%.3f)",
                            coupling > 0 ? "coupling" : "decoupling", coupling),
                scale,
                Math.abs(coupling)
            );
            couplingPattern.addCharacteristic("correlation", coupling);
            patterns.add(couplingPattern);
        }

        // Mind-wandering episodes
        double wanderingIntensity = Arrays.stream(wandering).average().orElse(0.0);
        double wanderingVariability = calculateVolatility(wandering);

        if (wanderingIntensity > 0.6) {
            Pattern wanderingPattern = new Pattern(
                "MindWanderingEpisode",
                String.format("Extended mind-wandering episode (intensity=%.3f)", wanderingIntensity),
                scale,
                wanderingIntensity
            );
            wanderingPattern.addCharacteristic("intensity", wanderingIntensity);
            wanderingPattern.addCharacteristic("variability", wanderingVariability);
            patterns.add(wanderingPattern);
        }

        // Attention switches
        int attentionSwitches = countAttentionSwitches(attention);
        if (attentionSwitches > attention.length * 0.3) {
            Pattern switchPattern = new Pattern(
                "FrequentAttentionSwitching",
                String.format("Frequent attention switching detected (%d switches)", attentionSwitches),
                scale,
                Math.min(1.0, (double) attentionSwitches / attention.length)
            );
            switchPattern.addCharacteristic("switch_count", attentionSwitches);
            switchPattern.addCharacteristic("switch_rate", (double) attentionSwitches / attention.length);
            patterns.add(switchPattern);
        }

        return patterns;
    }

    /**
     * Analyze patterns across multiple scales
     */
    private List<Pattern> analyzeCrossScalePatterns(List<double[]> timeSeries) {
        List<Pattern> patterns = new ArrayList<>();

        if (timeSeries.size() < 16) return patterns;

        // Multi-scale entropy analysis
        double entropy = calculateMultiScaleEntropy(timeSeries);
        if (entropy > 0.8) {
            Pattern entropyPattern = new Pattern(
                "HighComplexity",
                String.format("High multi-scale entropy detected (%.3f)", entropy),
                scale,
                Math.min(1.0, entropy)
            );
            entropyPattern.addCharacteristic("entropy", entropy);
            patterns.add(entropyPattern);
        }

        // Scale-invariant patterns
        double scaleInvariance = calculateScaleInvariance(timeSeries);
        if (scaleInvariance > 0.7) {
            Pattern invariancePattern = new Pattern(
                "ScaleInvariance",
                String.format("Scale-invariant pattern detected (%.3f)", scaleInvariance),
                scale,
                scaleInvariance
            );
            invariancePattern.addCharacteristic("scale_invariance", scaleInvariance);
            patterns.add(invariancePattern);
        }

        return patterns;
    }

    // Utility methods

    private double[] extractDimension(List<double[]> timeSeries, int dimension) {
        return timeSeries.stream()
            .filter(point -> point.length > dimension)
            .mapToDouble(point -> point[dimension])
            .toArray();
    }

    private double[] downsampleSeries(double[] series, int factor) {
        int newLength = series.length / factor;
        double[] downsampled = new double[newLength];

        for (int i = 0; i < newLength; i++) {
            // Average over the factor window
            double sum = 0;
            int count = 0;
            for (int j = i * factor; j < Math.min((i + 1) * factor, series.length); j++) {
                sum += series[j];
                count++;
            }
            downsampled[i] = count > 0 ? sum / count : 0;
        }

        return downsampled;
    }

    private double calculateCorrelation(double[] x, double[] y) {
        if (x.length != y.length || x.length == 0) return 0.0;

        double meanX = Arrays.stream(x).average().orElse(0.0);
        double meanY = Arrays.stream(y).average().orElse(0.0);

        double numerator = 0.0;
        double denomX = 0.0;
        double denomY = 0.0;

        for (int i = 0; i < x.length; i++) {
            double dx = x[i] - meanX;
            double dy = y[i] - meanY;
            numerator += dx * dy;
            denomX += dx * dx;
            denomY += dy * dy;
        }

        double denominator = Math.sqrt(denomX * denomY);
        return denominator == 0 ? 0.0 : numerator / denominator;
    }

    private double calculateTrend(double[] values) {
        if (values.length < 2) return 0.0;

        // Simple linear regression slope
        double n = values.length;
        double sumX = n * (n - 1) / 2; // Sum of indices 0,1,2,...,n-1
        double sumY = Arrays.stream(values).sum();
        double sumXY = IntStream.range(0, values.length)
            .mapToDouble(i -> i * values[i])
            .sum();
        double sumX2 = n * (n - 1) * (2 * n - 1) / 6; // Sum of squares of indices

        return (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
    }

    private double detectPeriodicity(double[] values) {
        if (values.length < 6) return 0.0;

        // Simple autocorrelation-based periodicity detection
        double maxCorrelation = 0.0;

        for (int lag = 1; lag < values.length / 2; lag++) {
            double correlation = calculateAutocorrelation(values, lag);
            maxCorrelation = Math.max(maxCorrelation, Math.abs(correlation));
        }

        return maxCorrelation;
    }

    private double calculateAutocorrelation(double[] values, int lag) {
        if (lag >= values.length) return 0.0;

        double[] x = Arrays.copyOfRange(values, 0, values.length - lag);
        double[] y = Arrays.copyOfRange(values, lag, values.length);

        return calculateCorrelation(x, y);
    }

    private double calculateVolatility(double[] values) {
        if (values.length < 2) return 0.0;

        double mean = Arrays.stream(values).average().orElse(0.0);
        double variance = Arrays.stream(values)
            .map(v -> Math.pow(v - mean, 2))
            .average()
            .orElse(0.0);

        return Math.sqrt(variance);
    }

    private double detectQuadraticRecurrence(double[] values) {
        if (values.length < 6) return 0.0;

        // Check if values[i+1] ≈ values[i]² + c for some constant c
        double bestFit = 0.0;

        for (double c = -1.0; c <= 1.0; c += 0.1) {
            double fitness = 0.0;
            int validPoints = 0;

            for (int i = 0; i < values.length - 1; i++) {
                double predicted = values[i] * values[i] + c;
                double actual = values[i + 1];
                double error = Math.abs(predicted - actual);

                if (error < 0.5) { // Threshold for "close enough"
                    fitness += 1.0 - error;
                    validPoints++;
                }
            }

            if (validPoints > 0) {
                fitness /= validPoints;
                bestFit = Math.max(bestFit, fitness);
            }
        }

        return bestFit;
    }

    private double detectFeedbackLoop(double[] values) {
        if (values.length < 4) return 0.0;

        // Look for patterns where current value influences future values
        double totalFeedback = 0.0;
        int validPairs = 0;

        for (int lag = 1; lag < Math.min(5, values.length / 2); lag++) {
            double correlation = calculateAutocorrelation(values, lag);
            if (Math.abs(correlation) > 0.3) {
                totalFeedback += Math.abs(correlation);
                validPairs++;
            }
        }

        return validPairs > 0 ? totalFeedback / validPairs : 0.0;
    }

    private int countAttentionSwitches(double[] attention) {
        if (attention.length < 2) return 0;

        int switches = 0;
        double threshold = 0.1; // Minimum change to consider a switch

        for (int i = 1; i < attention.length; i++) {
            if (Math.abs(attention[i] - attention[i-1]) > threshold) {
                switches++;
            }
        }

        return switches;
    }

    private double calculateMultiScaleEntropy(List<double[]> timeSeries) {
        if (timeSeries.isEmpty()) return 0.0;

        // Simplified multi-scale entropy calculation
        double[] firstDim = extractDimension(timeSeries, 0);

        // Calculate sample entropy
        return calculateSampleEntropy(firstDim, 2, 0.2);
    }

    private double calculateSampleEntropy(double[] data, int m, double r) {
        int n = data.length;
        if (n < m + 1) return 0.0;

        double[] template = new double[m];
        int matchCount = 0;
        int totalCount = 0;

        for (int i = 0; i < n - m; i++) {
            for (int j = 0; j < m; j++) {
                template[j] = data[i + j];
            }

            for (int k = i + 1; k < n - m; k++) {
                boolean matches = true;
                for (int j = 0; j < m; j++) {
                    if (Math.abs(template[j] - data[k + j]) > r) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    matchCount++;
                }
                totalCount++;
            }
        }

        return totalCount > 0 ? -Math.log((double) matchCount / totalCount) : 0.0;
    }

    private double calculateScaleInvariance(List<double[]> timeSeries) {
        if (timeSeries.size() < 16) return 0.0;

        double[] values = extractDimension(timeSeries, 0);

        // Compare statistics at different scales
        double[] scale2 = downsampleSeries(values, 2);
        double[] scale4 = downsampleSeries(values, 4);

        double var1 = calculateVolatility(values);
        double var2 = calculateVolatility(scale2);
        double var4 = calculateVolatility(scale4);

        // Scale invariance if variances follow power law
        if (var1 == 0 || var2 == 0 || var4 == 0) return 0.0;

        double ratio1 = var2 / var1;
        double ratio2 = var4 / var2;

        // Check if ratios are similar (indicating scale invariance)
        return 1.0 - Math.abs(ratio1 - ratio2) / Math.max(ratio1, ratio2);
    }

    private int countBoxes(double[] normalized, double boxSize) {
        Set<Integer> boxes = new HashSet<>();

        for (double value : normalized) {
            int boxIndex = (int) Math.floor(value / boxSize);
            boxes.add(boxIndex);
        }

        return boxes.size();
    }

    private double calculateLogLogSlope(List<Double> boxSizes, List<Integer> boxCounts) {
        if (boxSizes.size() != boxCounts.size() || boxSizes.size() < 2) {
            return 1.0;
        }

        // Convert to log scale
        double[] logSizes = boxSizes.stream()
            .filter(size -> size > 0)
            .mapToDouble(Math::log)
            .toArray();

        double[] logCounts = boxCounts.stream()
            .filter(count -> count > 0)
            .mapToDouble(count -> Math.log(count))
            .toArray();

        if (logSizes.length != logCounts.length || logSizes.length < 2) {
            return 1.0;
        }

        // Calculate slope using linear regression
        return Math.abs(calculateTrend(logCounts));
    }

    /**
     * Calculate pattern confidence based on Hurst exponent.
     *
     * @param hurstExponent Calculated Hurst exponent
     * @return Confidence score
     */
    private double calculatePatternConfidence(double hurstExponent) {
        // Map Hurst exponent to confidence score
        // 0.5 is a neutral point, above indicates persistence, below indicates mean reversion
        return Math.abs(hurstExponent - 0.5) * 2;
    }
}
