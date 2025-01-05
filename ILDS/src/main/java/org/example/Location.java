package org.example;

// Class for Node (Customer Location or Delivery Hub)
public class Location {
    private String id;
    private String name;
    private boolean isHub;    // true if Node is Hub and false if node is customer location

    public Location(String id, String name, boolean isHub) {
        this.id = id;
        this.name = name;
        this.isHub = isHub;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
