package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "assessments")
public class AssessmentRequest {
    @Id
    private String id;
    private double area;
    private double predictedRain;
    private double waterPotential;
    private double estimatedCost; // Added to fix the 0 output
    private LocalDateTime timestamp;

    // 1. MANDATORY: Default constructor for JSON mapping
    public AssessmentRequest() {
        this.timestamp = LocalDateTime.now();
    }

    // 2. Full Constructor used by the Controller
    public AssessmentRequest(double area, double predictedRain, double waterPotential, double estimatedCost) {
        this.area = area;
        this.predictedRain = predictedRain;
        this.waterPotential = waterPotential;
        this.estimatedCost = estimatedCost;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getId() { return id; }
    public double getArea() { return area; }
    public double getPredictedRain() { return predictedRain; }
    public double getWaterPotential() { return waterPotential; }
    public double getEstimatedCost() { return estimatedCost; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setters
    public void setArea(double area) { this.area = area; }
    public void setPredictedRain(double predictedRain) { this.predictedRain = predictedRain; }
    public void setWaterPotential(double waterPotential) { this.waterPotential = waterPotential; }
    public void setEstimatedCost(double estimatedCost) { this.estimatedCost = estimatedCost; }
}