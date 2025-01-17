package org.example;

import java.time.*;
import java.util.*;

public class CongestionPredictor {
    private TrafficData trafficData;
    private Map<String, Double> congestionThresholds;
    
    public CongestionPredictor() {
        this.trafficData = new TrafficData();
        this.congestionThresholds = new HashMap<>();
        initializeDefaultThresholds();
    }
    
    private void initializeDefaultThresholds() {
        // define congestion thresholds for different road types
        congestionThresholds.put("MAIN", 2.0);
        congestionThresholds.put("SECONDARY", 1.5);
        congestionThresholds.put("LOCAL", 1.2);
    }
    
    public double predictCongestion(Road road, LocalDateTime time) {
        double historicalCongestion = trafficData.getPredictedCongestion(road.getId(), time.getHour());
        double currentCongestion = road.getCurrentCongestion();
        
        // weight current conditions more heavily than historical data
        double weightCurrent = 0.7;
        double weightHistorical = 0.3;
        
        return (currentCongestion * weightCurrent) + (historicalCongestion * weightHistorical);
    }
    
    public boolean isLikelyBottleneck(Road road, LocalDateTime time) {
        double predictedCongestion = predictCongestion(road, time);
        String roadType = determineRoadType(road);
        return predictedCongestion > congestionThresholds.getOrDefault(roadType, 1.5);
    }
    
    private String determineRoadType(Road road) {
        // simple logic to determine road type based on average speed
        if (road.getAverageSpeed() > 80) return "MAIN";
        if (road.getAverageSpeed() > 50) return "SECONDARY";
        return "LOCAL";
    }
} 