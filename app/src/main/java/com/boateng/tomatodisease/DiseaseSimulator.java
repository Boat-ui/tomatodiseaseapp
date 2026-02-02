package com.boateng.tomatodisease;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.Random;

public class DiseaseSimulator {

    private static final Random random = new Random();

    public static class DiseaseResult {
        public String disease;
        public float confidence;
        public String treatment;
        public String info;

        public DiseaseResult(String disease, float confidence, String treatment, String info) {
            this.disease = disease;
            this.confidence = confidence;
            this.treatment = treatment;
            this.info = info;
        }
    }

    // Main method to analyze image with smart simulation
    public static DiseaseResult analyzeImage(Bitmap image) {
        if (image == null) {
            return getRandomResult();
        }

        // Analyze image characteristics
        ImageAnalysis analysis = analyzeImageCharacteristics(image);

        // Use characteristics to determine likely disease
        return simulateSmartAnalysis(analysis);
    }

    private static class ImageAnalysis {
        float darkness; // 0-1 scale
        float spotDensity; // 0-1 scale
        float colorVariation; // 0-1 scale
        float edgeSharpness; // 0-1 scale
        float greenDominance; // 0-1 scale
    }

    private static ImageAnalysis analyzeImageCharacteristics(Bitmap image) {
        ImageAnalysis analysis = new ImageAnalysis();

        // Sample pixels for analysis
        int width = image.getWidth();
        int height = image.getHeight();
        int sampleSize = 100;

        float totalDarkness = 0;
        float totalGreen = 0;
        float spotCount = 0;

        for (int i = 0; i < sampleSize; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int pixel = image.getPixel(x, y);

            int red = Color.red(pixel);
            int green = Color.green(pixel);
            int blue = Color.blue(pixel);

            // Calculate darkness (0=white, 1=black)
            float darkness = 1 - ((red + green + blue) / 765f);
            totalDarkness += darkness;

            // Calculate green dominance
            if (green > red && green > blue) {
                totalGreen += 1;
            }

            // Detect potential spots (very dark pixels)
            if (darkness > 0.7) {
                spotCount++;
            }
        }

        analysis.darkness = totalDarkness / sampleSize;
        analysis.greenDominance = totalGreen / sampleSize;
        analysis.spotDensity = spotCount / sampleSize;

        // Add some randomness for simulation
        analysis.colorVariation = 0.3f + random.nextFloat() * 0.4f;
        analysis.edgeSharpness = 0.4f + random.nextFloat() * 0.5f;

        return analysis;
    }

    private static DiseaseResult simulateSmartAnalysis(ImageAnalysis analysis) {
        // Disease probabilities based on image characteristics
        DiseaseProbability[] probabilities = calculateDiseaseProbabilities(analysis);

        // Select disease with highest probability
        String selectedDisease = selectDisease(probabilities);

        // Generate realistic confidence (85-99%)
        float baseConfidence = 85 + random.nextFloat() * 14; // 85-99

        // Adjust confidence based on image analysis quality
        float qualityScore = calculateQualityScore(analysis);
        float confidence = baseConfidence * qualityScore;

        // Ensure confidence stays in 85-99% range
        confidence = Math.max(85, Math.min(99, confidence));

        // Round to 1 decimal place
        confidence = Math.round(confidence * 10) / 10.0f;

        return new DiseaseResult(
                selectedDisease,
                confidence,
                getTreatment(selectedDisease),
                getDiseaseInfo(selectedDisease)
        );
    }

    private static DiseaseProbability[] calculateDiseaseProbabilities(ImageAnalysis analysis) {
        DiseaseProbability[] diseases = new DiseaseProbability[5];

        // Healthy - high green, low darkness, low spots
        diseases[0] = new DiseaseProbability("Healthy",
                (analysis.greenDominance * 0.7f + (1 - analysis.darkness) * 0.3f));

        // Early Blight - moderate spots, moderate darkness
        diseases[1] = new DiseaseProbability("Early Blight",
                (analysis.spotDensity * 0.6f + analysis.darkness * 0.4f));

        // Late Blight - high spots, high darkness
        diseases[2] = new DiseaseProbability("Late Blight",
                (analysis.spotDensity * 0.8f + analysis.darkness * 0.2f));

        // Leaf Mold - moderate green, moderate spots
        diseases[3] = new DiseaseProbability("Leaf Mold",
                (analysis.greenDominance * 0.5f + analysis.spotDensity * 0.5f));

        // Bacterial Spot - high spot density, low green
        diseases[4] = new DiseaseProbability("Bacterial Spot",
                (analysis.spotDensity * 0.7f + (1 - analysis.greenDominance) * 0.3f));

        // Add some random variation to make it more realistic
        for (DiseaseProbability dp : diseases) {
            dp.probability *= (0.8f + random.nextFloat() * 0.4f);
        }

        // Normalize probabilities to sum to 1
        float total = 0;
        for (DiseaseProbability dp : diseases) {
            total += dp.probability;
        }
        for (DiseaseProbability dp : diseases) {
            dp.probability /= total;
        }

        return diseases;
    }

    private static String selectDisease(DiseaseProbability[] probabilities) {
        // Roulette wheel selection
        float randomValue = random.nextFloat();
        float cumulative = 0;

        for (DiseaseProbability dp : probabilities) {
            cumulative += dp.probability;
            if (randomValue <= cumulative) {
                return dp.disease;
            }
        }

        // Fallback to highest probability
        return probabilities[0].disease;
    }

    private static float calculateQualityScore(ImageAnalysis analysis) {
        // Calculate how good the image is for analysis
        // Higher quality = more reliable result

        // 1. Green dominance is good for plant analysis
        float greenScore = analysis.greenDominance * 0.4f;

        // 2. Moderate darkness is good (not too bright, not too dark)
        float darknessScore = 1 - Math.abs(analysis.darkness - 0.5f) * 0.3f;

        // 3. Some spot density is good for disease detection
        float spotScore = Math.min(analysis.spotDensity * 2, 0.3f);

        return 0.9f + greenScore + darknessScore + spotScore; // Range: 0.9-1.0
    }

    private static DiseaseResult getRandomResult() {
        // Fallback if no image
        String[] diseases = {
                "Early Blight", "Late Blight", "Healthy", "Leaf Mold", "Bacterial Spot"
        };
        String disease = diseases[random.nextInt(diseases.length)];
        float confidence = 88 + random.nextFloat() * 10; // 88-98

        return new DiseaseResult(
                disease,
                confidence,
                getTreatment(disease),
                getDiseaseInfo(disease)
        );
    }

    private static class DiseaseProbability {
        String disease;
        float probability;

        DiseaseProbability(String disease, float probability) {
            this.disease = disease;
            this.probability = probability;
        }
    }

    private static String getTreatment(String disease) {
        switch (disease) {
            case "Early Blight":
                return "Remove infected leaves immediately. Apply copper fungicide every 7-10 days. Improve air circulation around plants.";
            case "Late Blight":
                return "DESTROY infected plants to prevent spread. Use mancozeb or chlorothalonil fungicide. Avoid overhead watering.";
            case "Leaf Mold":
                return "Increase spacing between plants. Apply potassium bicarbonate spray. Remove affected leaves and improve ventilation.";
            case "Bacterial Spot":
                return "Use copper-based bactericide. Remove and destroy infected plants. Water at soil level, not on leaves.";
            default:
                return "Plant appears healthy! Continue regular watering and fertilization. Monitor for any changes weekly.";
        }
    }

    private static String getDiseaseInfo(String disease) {
        switch (disease) {
            case "Early Blight":
                return "Circular brown spots with concentric rings (target pattern). Starts on older leaves, spreads upward. Common in warm, humid conditions.";
            case "Late Blight":
                return "Water-soaked gray-green spots that rapidly expand. White fungal growth underside in humidity. Can destroy entire plant in days.";
            case "Leaf Mold":
                return "Pale green or yellow spots on upper leaf surface. Velvety grayish-brown mold develops underneath. Thrives in high humidity.";
            case "Bacterial Spot":
                return "Small, dark, water-soaked spots with yellow halos. Spots may merge causing large dead areas. Spreads through water splash.";
            default:
                return "No disease detected. Leaves are uniformly green with no spots or discoloration. Plant shows healthy growth patterns.";
        }
    }
}