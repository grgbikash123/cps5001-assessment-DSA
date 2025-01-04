package org.example;


import java.util.*;
import java.time.*;

public class Main {


    private static void testPathFinding(LogisticsNetwork network) {
        System.out.println("\n-----------------------------------Testing path finding starts----------------------------------------------\n");

        // Test 1: Find path from Central Hub to Northeast Customer
        System.out.println("\nPath from Central Hub (H1) to Northeast Customer (C6):");
        System.out.println("\tShortest path  (Distance) : " + network.findPath("H1", "C6", true));
        System.out.println("\tFastest path (Travel Time): " + network.findPath("H1", "C6", false));
        System.out.println("\n------------------------------------Testing path finding ends----------------------------------------------\n");

        // Test 2: Find path from West Hub to East Customer
//        System.out.println("\nPath from West Hub (H5) to East Customer (C3):");
//        System.out.println("\tShortest path  (Distance) : " + network.findPath("H5", "C3", true));
//        System.out.println("\tFastest path (Travel Time): " + network.findPath("H5", "C3", false));
//
//        // Test 3: Find path between distant customers
//        System.out.println("\nPath from Southwest Customer (C7) to Northeast Customer (C6):");
//        System.out.println("\tShortest path  (Distance) : " + network.findPath("C7", "C6", true));
//        System.out.println("\tFastest path (Travel Time): " + network.findPath("C7", "C6", false));
    }


    private static void testDeliveryScheduling(LogisticsNetwork network) {

        System.out.println("\n-----------------------------------Testing Delivery Scheduling System starts----------------------------------------------\n");

        DeliveryScheduler scheduler = new DeliveryScheduler(network);
        
        // Create vehicles
        Vehicle vehicle1 = new Vehicle("V1", 15.0, network.getLocation("H1"));
        Vehicle vehicle2 = new Vehicle("V2", 20.0, network.getLocation("H2"));
        Vehicle vehicle3 = new Vehicle("V3", 10.0, network.getLocation("H3"));
        
        scheduler.addVehicle(vehicle1);
        scheduler.addVehicle(vehicle2);
        scheduler.addVehicle(vehicle3);
        
        // Create deliveries with different priorities and deadlines
        LocalDateTime now = LocalDateTime.now();

        System.out.println("=== Delivery Details ===");

        Delivery d1 = new Delivery("D1", "C1", 10.0, now.plusHours(2), 1.5, Delivery.DeliveryPriority.HIGH);
        Delivery d2 = new Delivery("D2", "C4", 7.0, now.plusHours(10), 2.0, Delivery.DeliveryPriority.MEDIUM);
        Delivery d3 = new Delivery("D3", "C7", 10.0, now.plusHours(15), 1.0, Delivery.DeliveryPriority.LOW);
        Delivery d4 = new Delivery("D4", "C2", 6.0, now.plusHours(2), 1.0, Delivery.DeliveryPriority.HIGH);

        d1.printDelivery();
        d2.printDelivery();
        d3.printDelivery();
        d4.printDelivery();

        System.out.println("==================================================================================================================================");
        
        scheduler.addDelivery(d1);
        scheduler.addDelivery(d2);
        scheduler.addDelivery(d3);
        scheduler.addDelivery(d4);
        
        // Schedule deliveries and display results
        scheduler.scheduleDeliveries();
        scheduler.displaySchedule();

        Delivery d5 = new Delivery("D5", "C3", 5.0, now.plusHours(9), 1.0, Delivery.DeliveryPriority.LOW);
        Delivery d6 = new Delivery("D6", "C6", 6.0, now.plusHours(4), 1.0, Delivery.DeliveryPriority.HIGH);

        d5.printDelivery();
        d6.printDelivery();

        scheduler.addDelivery(d5);
        scheduler.addDelivery(d6);

        scheduler.scheduleDeliveries();
        scheduler.displaySchedule();


        System.out.println("\n------------------------------------Testing Delivery Scheduling System ends----------------------------------------------\n");
    }



    public static void main(String[] args) {
        // Create a LogisticsNetwork instance
        LogisticsNetwork network = new LogisticsNetwork();

        // Create hub locations
        Location hub1 = new Location("H1", "Central Hub", true);
        Location hub2 = new Location("H2", "North Hub", true);
        Location hub3 = new Location("H3", "South Hub", true);
        Location hub4 = new Location("H4", "East Hub", true);
        Location hub5 = new Location("H5", "West Hub", true);

        // Create customer locations
        Location customer1 = new Location("C1", "Customer North", false);
        Location customer2 = new Location("C2", "Customer South", false);
        Location customer3 = new Location("C3", "Customer East", false);
        Location customer4 = new Location("C4", "Customer West", false);
        Location customer5 = new Location("C5", "Customer Central", false);
        Location customer6 = new Location("C6", "Customer Northeast", false);
        Location customer7 = new Location("C7", "Customer Southwest", false);

        // Add all locations to the network
        network.addLocation(hub1);
        network.addLocation(hub2);
        network.addLocation(hub3);
        network.addLocation(hub4);
        network.addLocation(hub5);
        network.addLocation(customer1);
        network.addLocation(customer2);
        network.addLocation(customer3);
        network.addLocation(customer4);
        network.addLocation(customer5);
        network.addLocation(customer6);
        network.addLocation(customer7);

        // Create roads connecting locations with different distances and conditions
        // Central connections
        network.addRoad(new Road("R1", hub1, customer5, 30));  // Central hub to central customer
        network.addRoad(new Road("R2", hub1, hub2, 80));       // Central to North hub
        network.addRoad(new Road("R3", hub1, hub3, 75));       // Central to South hub
        network.addRoad(new Road("R4", hub1, hub4, 90));       // Central to East hub
        network.addRoad(new Road("R5", hub1, hub5, 85));       // Central to West hub

        // North area connections
        network.addRoad(new Road("R6", hub2, customer1, 40));  // North hub to north customer
        network.addRoad(new Road("R7", hub2, customer6, 35));  // North hub to northeast customer
        network.addRoad(new Road("R8", hub2, hub4, 60));       // North hub to East hub

        // South area connections
        network.addRoad(new Road("R9", hub3, customer2, 40));  // South hub to south customer
        network.addRoad(new Road("R10", hub3, customer7, 30)); // South hub to southwest customer
        network.addRoad(new Road("R11", hub3, hub5, 58));      // South hub to West hub

        // East area connections
        network.addRoad(new Road("R12", hub4, customer3, 42)); // East hub to east customer
        network.addRoad(new Road("R13", hub4, customer6, 33)); // East hub to northeast customer

        // West area connections
        network.addRoad(new Road("R14", hub5, customer4, 30)); // West hub to west customer
        network.addRoad(new Road("R15", hub5, customer7, 35)); // West hub to southwest customer

        // Add some cross connections
        network.addRoad(new Road("R16", customer5, customer1, 70)); // Central to North customer
        network.addRoad(new Road("R17", customer5, customer3, 65)); // Central to East customer
        network.addRoad(new Road("R18", customer6, customer3, 28)); // Northeast to East customer
        network.addRoad(new Road("R19", customer7, customer4, 30)); // Southwest to West customer

        // Add some traffic conditions
        Road congested1 = new Road("R20", hub1, hub4, 75); // Alternative route to East hub
        congested1.setCurrentCongestion(2.0); // High traffic
        network.addRoad(congested1);

        Road congested2 = new Road("R21", customer5, customer2, 60);
        congested2.setCurrentCongestion(1.5); // Medium traffic
        network.addRoad(congested2);

        // Display the initial network
        System.out.println("Initial Network Structure:");
        network.displayNetwork();


        // Test pathfinding with different scenarios
        testPathFinding(network);

        testDeliveryScheduling(network);

/*
        // Setup delivery requirements
        Map<String, Double> deliveryLoads = new HashMap<>();
        deliveryLoads.put("C1", 5.0);  // 5 units to Customer 1
        deliveryLoads.put("C4", 8.0);  // 8 units to Customer 3
        deliveryLoads.put("C7", 4.0);  // 4 units to Customer 6

        Map<String, Double> deadlines = new HashMap<>();
        deadlines.put("C1", 10.0);  // 2 hours deadline
        deadlines.put("C4", 10.0);  // 3 hours deadline
        deadlines.put("C7", 10.0);  // 2.5 hours deadline

        double vehicleCapacity = 30.0;  // Maximum vehicle capacity

        // Find optimal delivery route
        List<String> optimalRoute = network.findOptimalDeliveryRoute(
                "H3",           // Start from Hub 1
                deliveryLoads, // Delivery requirements
                deadlines,     // Time constraints
                vehicleCapacity // Vehicle constraints
        );

        System.out.println("Optimal delivery route: " + optimalRoute);
*/


        // Test adaptive routing with congestion prediction
        System.out.println("\nTesting adaptive routing with congestion prediction:");
        LocalDateTime currentTime = LocalDateTime.now();

        // Test route during peak hours
        LocalDateTime peakHour = LocalDateTime.of(
            LocalDate.now(), LocalTime.of(8, 0)); // 8:00 AM
        System.out.println("\nFinding route during peak hours (8:00 AM) from H1 to C6:");
        List<String> peakHourRoute = network.findAdaptiveRoute("H1", "C6", peakHour);
        System.out.println("Adaptive route during peak hours: " + peakHourRoute);


        // Test route during peak hours
        LocalDateTime morningHour = LocalDateTime.of(
                LocalDate.now(), LocalTime.of(6, 0)); // 8:00 AM
        System.out.println("\nFinding route during peak hours (6:00 AM) from H1 to C6:");
        List<String> morningHourRoute = network.findAdaptiveRoute("H1", "C6", morningHour);
        System.out.println("Adaptive route during peak hours: " + morningHourRoute);


        // Test route during off-peak hours
        LocalDateTime offPeakHour = LocalDateTime.of(
            LocalDate.now(), LocalTime.of(14, 0)); // 2:00 PM
        System.out.println("\nFinding route during off-peak hours (2:00 PM) from H1 to C6:");
        List<String> offPeakRoute = network.findAdaptiveRoute("H1", "C6", offPeakHour);
        System.out.println("Adaptive route during off-peak hours: " + offPeakRoute);
    }
}