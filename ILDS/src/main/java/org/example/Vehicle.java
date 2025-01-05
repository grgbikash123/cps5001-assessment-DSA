package org.example;

import org.example.Location;
import java.util.*;

public class Vehicle {
    private String id;
    private double capacity;
    private double currentLoad;
    private Location currentLocation;
    private List<Delivery> assignedDeliveries;
    private boolean available;
    
    public Vehicle(String id, double capacity, Location startLocation) {
        this.id = id;
        this.capacity = capacity;
        this.currentLoad = 0.0;
        this.currentLocation = startLocation;
        this.assignedDeliveries = new ArrayList<>();
        this.available = true;
    }
    
    public boolean canAcceptDelivery(Delivery delivery) {
        return available && (currentLoad + delivery.getLoad() <= capacity);
    }
    
    public void assignDelivery(Delivery delivery) {
        if (canAcceptDelivery(delivery)) {
            assignedDeliveries.add(delivery);
            currentLoad += delivery.getLoad();
            if (currentLoad >= capacity) {
                available = false;
            }
        }
    }

    public void getDetailsTable() {
        System.out.printf("Vehicle %s (Load: %.2f/%.2f, Available: %b)\n",
                id, currentLoad, capacity, available);
    }
    
    public String getId() { return id; }
    public double getCapacity() { return capacity; }
    public double getCurrentLoad() { return currentLoad; }
    public Location getCurrentLocation() { return currentLocation; }
    public List<Delivery> getAssignedDeliveries() { return assignedDeliveries; }
    public boolean isAvailable() { return available; }
} 
 