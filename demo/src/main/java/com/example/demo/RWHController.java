package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*") 
public class RWHController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping("/calculate")
    public Map<String, Object> calculateAssessment(@RequestBody Map<String, Double> request) {
        double roofArea = request.get("area");
        
        // 1. Get Smart Data (History + Prediction)
        Map<String, Object> weatherData = weatherService.getRainfallAnalysis(19.07, 72.87);
        double predictedRain = (double) weatherData.get("prediction");

        // 2. Calculate
        double totalHarvestedWater = roofArea * predictedRain * 0.85;
        double tankSize = totalHarvestedWater * 0.15;
        double cost = (tankSize * 6.0) + 5000.0;
        
        // 3. Send Everything back
        Map<String, Object> report = new HashMap<>();
        report.put("litersSaved", Math.round(totalHarvestedWater));
        report.put("tankSize", Math.round(tankSize));
        report.put("totalCost", Math.round(cost));
        report.put("rainfallUsed", Math.round(predictedRain));
        report.put("history", weatherData.get("history")); // <--- SENDING HISTORY FOR GRAPH
        
        return report;
    }
}