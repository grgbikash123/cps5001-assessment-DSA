package org.example;

import org.example.Location;
import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class DeliveryScheduler {
    private PriorityQueue<Delivery> deliveryQueue;
    private Map<String, Vehicle> availableVehicles;
    private Map<String, List<Delivery>> scheduledDeliveries;
    private LogisticsNetwork logisticsNetwork; // Add this field

    
    public DeliveryScheduler(LogisticsNetwork logisticsNetwork) {
        this.logisticsNetwork = logisticsNetwork;
        // Initialize with custom comparator for delivery prioritization
        this.deliveryQueue = new PriorityQueue<>((d1, d2) -> {
            // First compare by priority
            int priorityCompare = d2.getPriority().getValue() - d1.getPriority().getValue();
            if (priorityCompare != 0) return priorityCompare;
            
            // Then by deadline
            return d1.getDeadline().compareTo(d2.getDeadline());
        });
        
        this.availableVehicles = new HashMap<>();
        this.scheduledDeliveries = new HashMap<>();
    }
    
    public void addDelivery(Delivery delivery) {
        deliveryQueue.offer(delivery);
    }
    
    public void addVehicle(Vehicle vehicle) {
        availableVehicles.put(vehicle.getId(), vehicle);
        scheduledDeliveries.put(vehicle.getId(), new ArrayList<>());
    }
    
    public Map<String, List<Delivery>> scheduleDeliveries() {
        while (!deliveryQueue.isEmpty()) {
            Delivery delivery = deliveryQueue.poll();
            Vehicle bestVehicle = findBestVehicle(delivery);
            
            if (bestVehicle != null) {
                bestVehicle.assignDelivery(delivery);
                scheduledDeliveries.get(bestVehicle.getId()).add(delivery);
            } else {
                System.out.println("Warning: Could not assign delivery " + delivery.getId() + 
                                 " - No suitable vehicle available");
            }
        }
        return scheduledDeliveries;
    }
    
    private Vehicle findBestVehicle(Delivery delivery) {
        Vehicle bestVehicle = null;
        double bestScore = Double.MAX_VALUE;
        
        for (Vehicle vehicle : availableVehicles.values()) {
            if (vehicle.canAcceptDelivery(delivery)) {
                double score = calculateAssignmentScore(vehicle, delivery);
                if (score < bestScore) {
                    bestScore = score;
                    bestVehicle = vehicle;
                }
            }
        }
        return bestVehicle;
    }
    
    private double calculateAssignmentScore(Vehicle vehicle, Delivery delivery) {
        // Lower score is better
        double score = 0.0;
        
        // Consider current load
        score += (vehicle.getCurrentLoad() / vehicle.getCapacity()) * 100;
        
        // Consider number of existing deliveries
        score += scheduledDeliveries.get(vehicle.getId()).size() * 10;
        
        // Consider deadline urgency
        Duration timeUntilDeadline = Duration.between(
            LocalDateTime.now(), 
            delivery.getDeadline()
        );
        score -= timeUntilDeadline.toHours() * 5;
        
        return score;
    }
    
    public void displaySchedule() {
        System.out.println("\n=== Delivery Schedule ===");
        if (scheduledDeliveries.isEmpty()) {
            System.out.println("No deliveries scheduled.");
            return;
        }

        // Define the formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        for (Map.Entry<String, List<Delivery>> entry : scheduledDeliveries.entrySet()) {
            String vehicleId = entry.getKey();
            List<Delivery> deliveries = entry.getValue();
            Vehicle vehicle = availableVehicles.get(vehicleId);

            System.out.println("\nVehicle: " + vehicleId);
            System.out.println("--------------------------------------------------------------------------------");
            if (deliveries.isEmpty()) {
                System.out.println("No deliveries assigned");
            } else {


                // System.out.printf("%-6s %-8s %-12s %-25s %-8s %s%n", 
                //     "ID", "Load", "Destination", "Deadline", "Est.Time", "Priority");
                System.out.printf("%-6s %-8s %-12s %-25s %-8s %-10s %-30s%n", 
                    "ID", "Load", "Destination", "Deadline", "Est.Time", "Priority", "Route");
                String currentLocation = vehicle.getCurrentLocation().getId();


                System.out.println("--------------------------------------------------------------------------------");


                for (int i = 0; i < deliveries.size(); i++) {
                    Delivery d = deliveries.get(i);
                    List<String> route = logisticsNetwork.findPath(currentLocation, d.getDestinationId(), false);
                    String routeStr = String.join(" → ", route);

                    // Calculate actual travel time using the network
                    double travelTime = logisticsNetwork.calculatePathTime(route);

                    // System.out.printf("%-6s %-8.2f %-12s %-25s %-8.2f %s%n",
                    //     d.getId(),
                    //     d.getLoad(),
                    //     d.getDestinationId(),
                    //     d.getDeadline().format(formatter),
                    //     d.getEstimatedTime(),
                    //     d.getPriority());
                    System.out.printf("%-6s %-8.2f %-12s %-25s %-8.2f %-10s %-30s%n",
                        d.getId(),
                        d.getLoad(),
                        d.getDestinationId(),
                        d.getDeadline().format(formatter),
                        travelTime,  // Using actual calculated travel time
                        d.getPriority(),
                        routeStr);
                    // Update current location for next delivery
                    currentLocation = d.getDestinationId();

                }
            }
        }
        System.out.println("================================================================================");
    }
}