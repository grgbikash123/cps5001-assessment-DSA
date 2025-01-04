package org.example;

import java.time.*;
import java.util.*;

public class TrafficData {
    // Store historical congestion data: roadId -> hour -> congestion records
    private Map<String, Map<Integer, List<Double>>> historicalCongestion;
    // Store current traffic patterns: roadId -> current congestion
    private Map<String, Double> currentTrafficPatterns;
    
    public TrafficData() {
        this.historicalCongestion = new HashMap<>();
        this.currentTrafficPatterns = new HashMap<>();
    }
    
    public void recordCongestion(String roadId, LocalDateTime timestamp, double congestionLevel) {
        int hour = timestamp.getHour();
        historicalCongestion
            .computeIfAbsent(roadId, k -> new HashMap<>())
            .computeIfAbsent(hour, k -> new ArrayList<>())
            .add(congestionLevel);
        
        currentTrafficPatterns.put(roadId, congestionLevel);
    }
    
    public double getPredictedCongestion(String roadId, int hour) {
        Map<Integer, List<Double>> roadData = historicalCongestion.get(roadId);
        if (roadData == null || !roadData.containsKey(hour)) {
            return 1.0; // Default congestion if no data
        }
        
        List<Double> hourlyData = roadData.get(hour);
        return hourlyData.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(1.0);
    }
} 
