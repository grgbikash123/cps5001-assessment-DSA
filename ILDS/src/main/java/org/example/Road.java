package org.example;

// Class for edge connecting Hubs and Customer Locations
public class Road {
    private String id;
    private Location source;
    private Location destination;
    private double distance;
    private double currentCongestion;
    private double averageSpeed;

    public Road(String id, Location source, Location destination, double distance) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.currentCongestion = 1.0; // default congestion factor
        this.averageSpeed = 50.0; // default speed in km/h
    }

    public double getTravelTime() { return (distance / averageSpeed) * currentCongestion; }

    public Location getSource() { return source; }

    public Location getDestination() { return destination; }

    public String getId() { return id; }

    public double getDistance() { return distance; }

    public double getCurrentCongestion() { return currentCongestion; }

    public double getAverageSpeed() { return averageSpeed; }


    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCurrentCongestion(double currentCong) {
        this.currentCongestion = currentCong;
    }

    public void setAverageSpeed(double avgSpeed) {
        this.averageSpeed = avgSpeed;
    }
}

