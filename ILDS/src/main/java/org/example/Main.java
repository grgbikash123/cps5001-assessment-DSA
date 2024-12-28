package org.example;
import java.util.*;
public class Main {
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
    }
}