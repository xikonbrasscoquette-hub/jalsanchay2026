package demo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class WeatherService {

    @SuppressWarnings("unchecked")
    public Map<String, Object> getRainfallAnalysis(double lat, double lon) {
        Map<String, Object> result = new HashMap<>();
        try {
            // Fetch 5 Years of Data
            String url = String.format("https://archive-api.open-meteo.com/v1/archive?latitude=%f&longitude=%f&start_date=2019-01-01&end_date=2023-12-31&daily=rain_sum&timezone=auto", lat, lon);
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> daily = (Map<String, Object>) response.get("daily");
            List<Double> dailyRain = (List<Double>) daily.get("rain_sum");

            // Group by Year
            double[] yearlyRainfall = new double[5];
            int daysPerYear = 365;
            for(int i=0; i<5; i++) {
                double yearSum = 0;
                for(int j=0; j<daysPerYear; j++) {
                    int index = (i * daysPerYear) + j;
                    if(index < dailyRain.size() && dailyRain.get(index) != null) yearSum += dailyRain.get(index);
                }
                yearlyRainfall[i] = Math.round(yearSum * 10.0) / 10.0; // Round to 1 decimal
            }

            // AI Prediction (Linear Regression)
            double n = 5, sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;
            for (int i = 0; i < n; i++) {
                sumX += i; sumY += yearlyRainfall[i]; sumXY += i * yearlyRainfall[i]; sumXX += i * i;
            }
            double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
            double intercept = (sumY - slope * sumX) / n;
            double predicted = (slope * 5) + intercept; // Year 6

            result.put("history", yearlyRainfall); // [2100, 3400, ...]
            result.put("prediction", predicted > 0 ? predicted : 1200);
            result.put("years", new int[]{2019, 2020, 2021, 2022, 2023});
            
        } catch (Exception e) {
            result.put("prediction", 1200.0);
            result.put("history", new double[]{1200, 1200, 1200, 1200, 1200});
        }
        return result;
    }
}