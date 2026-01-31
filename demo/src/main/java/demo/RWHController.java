package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.AssessmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Map;
import java.util.HashMap;

// 1. Database Tool
interface AssessmentRepository extends MongoRepository<AssessmentRequest, String> {}

@RestController
@CrossOrigin(origins = "*") 
public class RWHController {

    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private AssessmentRepository repository; 

    @PostMapping("/calculate")
    public Map<String, Object> calculateAssessment(@RequestBody Map<String, Object> request) {
        
        // --- STEP 1: CLEAN THE INPUT ---
        String rawArea = request.get("area").toString();
        String cleanArea = rawArea.replaceAll("[^0-9.]", "");
        double roofArea = Double.parseDouble(cleanArea);
        
        // --- STEP 2: DO THE MATH ---
        // Using Mumbai coordinates as default
        Map<String, Object> weatherData = weatherService.getRainfallAnalysis(19.07, 72.87);
        double predictedRain = (double) weatherData.get("prediction"); 
        
        // Rainwater Harvesting Logic
        double potential = roofArea * predictedRain * 0.85; // 0.85 is the runoff coefficient
        double cost = potential * 5.5; // Example: ₹5.5 per liter of storage capacity

        // --- STEP 3: SAVE TO CLOUD ---
        // Ensure your AssessmentRequest constructor accepts: (area, predictedRain, potential, cost)
        AssessmentRequest record = new AssessmentRequest(roofArea, predictedRain, potential, cost);
        repository.save(record);
        System.out.println("✅ Data Saved to MongoDB Atlas!");

        // --- STEP 4: SEND RESPONSE (Keys must match index.html exactly) ---
        Map<String, Object> report = new HashMap<>();
        report.put("prediction", predictedRain);         // JS: data.prediction
        report.put("harvestPotential", potential);       // JS: data.harvestPotential
        report.put("tankSize", potential * 1.1);         // JS: data.tankSize
        report.put("estimatedCost", cost);               // JS: data.estimatedCost
        report.put("history", weatherData.get("history"));
        
        return report;
    }
}