package org.example;
import java.util.*;

public class LogisticsNetwork {

    // LocationID = key, Location = value      ["H1": ("H1", "Hub1", true), "L1": ("L1", "Location1", False).....]
    private Map<String, Location> locations; // Nodes of the graph

    // LocationID = key, Set of Roads = value
    // ["H1" : (
    //           ("R1", (source Location), (destination Location), distance=400.0, cogestion=1, avgSpeed=50),
    //           ("R2", (source Location), (destination Location), distance=500.0, cogestion=1, avgSpeed=50)
    //         ),
    //  "L1" : (
    //          ("R1", (source Location), (destination Location), distance=400.0, cogestion=1, avgSpeed=50),
    //           ("R2", (source Location), (destination Location), distance=500.0, cogestion=1, avgSpeed=50)
    //          )]
    private Map<String, Set<Road>> adjacencyList; // Adjacency list for edges


    public LogisticsNetwork() {
        this.locations = new HashMap<>();
        this.adjacencyList = new HashMap<>();
    }


    // Add a new location (node) to the network
    public void addLocation(Location location) {
        if (locations.containsKey(location.getId())) {
            System.out.println("Location already exists: " + location.getId());
            return;
        }
        locations.put(location.getId(), location);
        adjacencyList.put(location.getId(), new HashSet<>());
    }

    // Remove a location (node) and its associated roads
    public void removeLocation(String locationId) {
        if (!locations.containsKey(locationId)) {
            System.out.println("Location not found: " + locationId);
            return;
        }
        locations.remove(locationId);
        adjacencyList.remove(locationId);

        // Remove roads connected to this location
        for (Set<Road> roads : adjacencyList.values()) {
            roads.removeIf(road -> road.getSource().getId().equals(locationId) || road.getDestination().getId().equals(locationId));
        }
    }

    // Add a new road (edge) to the network
    public void addRoad(Road road) {
        if (road == null) throw new IllegalArgumentException("Road cannot be null");
        String sourceId = road.getSource().getId();
        String destinationId = road.getDestination().getId();

        if (!locations.containsKey(sourceId) || !locations.containsKey(destinationId)) {
            System.out.println("Source or destination location not found");
            return;
        }

        adjacencyList.get(sourceId).add(road);

        // For undirected graph, add reverse edge
        Road reverseRoad = new Road(road.getId() + "_reverse", road.getDestination(), road.getSource(), road.getDistance());
        reverseRoad.setAverageSpeed(road.getAverageSpeed());
        reverseRoad.setCurrentCongestion(road.getCurrentCongestion());
        adjacencyList.get(destinationId).add(reverseRoad);
    }

    // Remove a road (edge) from the network
    public void removeRoad(String roadId) {
        for (Set<Road> roads : adjacencyList.values()) {
            roads.removeIf(road -> road.getId().equals(roadId));
        }
    }

    // Update the attributes of an existing road
    public void updateRoad(String roadId, double newDistance, double newCongestion, double newSpeed) {
        for (Set<Road> roads : adjacencyList.values()) {
            for (Road road : roads) {
                if (road.getId().equals(roadId)) {
                    road.setDistance(newDistance);
                    road.setCurrentCongestion(newCongestion);
                    road.setAverageSpeed(newSpeed);
                    return;
                }
            }
        }
        System.out.println("Road not found: " + roadId);
    }

    // Get all roads connected to a location
    public Set<Road> getConnectedRoads(String locationId) {
        return adjacencyList.getOrDefault(locationId, new HashSet<>());
    }

    // Display the current state of the network
    public void displayNetwork() {
        for (String locationId : adjacencyList.keySet()) {
            System.out.println("Location: " + locationId);
            for (Road road : adjacencyList.get(locationId)) {
                System.out.println("  -> Road to " + road.getDestination().getId() + " | Distance: " + road.getDistance() +
                        " | Congestion: " + road.getCurrentCongestion() + " | Avg Speed: " + road.getAverageSpeed() + " | Travel Time: " + road.getTravelTime());
            }
        }
    }

}