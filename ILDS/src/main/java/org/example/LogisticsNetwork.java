package org.example;
import java.util.*;
import java.time.*;

public class LogisticsNetwork {

    // LocationID = key, Location = value      ["H1": ("H1", "Hub1", true), "L1": ("L1", "Location1", False).....]
    private Map<String, Location> locations; // Nodes of the graph : O(V)

    // LocationID = key, Set of Roads = value
    // ["H1" : (
    //           ("R1", (source Location), (destination Location), distance=400.0, cogestion=1, avgSpeed=50),
    //           ("R2", (source Location), (destination Location), distance=500.0, cogestion=1, avgSpeed=50)
    //         ),
    //  "L1" : (
    //          ("R1", (source Location), (destination Location), distance=400.0, cogestion=1, avgSpeed=50),
    //           ("R2", (source Location), (destination Location), distance=500.0, cogestion=1, avgSpeed=50)
    //          )]
    private Map<String, Set<Road>> adjacencyList; // Adjacency list for edges O(V + E)

    private CongestionPredictor congestionPredictor;


    public LogisticsNetwork() {
        this.locations = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.congestionPredictor = new CongestionPredictor();
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

    public Location getLocation(String locationId) {
        Location location = locations.get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationId);
        }
        return location;
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
    public void displayNetwork1() {
        for (String locationId : adjacencyList.keySet()) {
            System.out.println("Location: " + locationId);
            for (Road road : adjacencyList.get(locationId)) {
                System.out.println("  -> Road to " + road.getDestination().getId() + " | Distance: " + road.getDistance() +
                        " | Congestion: " + road.getCurrentCongestion() + " | Avg Speed: " + road.getAverageSpeed() + " | Travel Time: " + road.getTravelTime());
            }
        }
    }

    public void displayNetwork() {
        System.out.println("\n=== Network Connectivity Details ===");
        System.out.printf("%-8s %-8s %-10s %-12s %-12s %-12s%n",
            "From", "To", "Distance", "Congestion", "Avg Speed", "Travel Time");
        System.out.println("------------------------------------------------------------------------");
        
        for (String locationId : adjacencyList.keySet()) {
            for (Road road : adjacencyList.get(locationId)) {
                System.out.printf("%-8s %-8s %-10.2f %-12.2f %-12.2f %-12.2f%n",
                    locationId,
                    road.getDestination().getId(),
                    road.getDistance(),
                    road.getCurrentCongestion(),
                    road.getAverageSpeed(),
                    road.getTravelTime());
            }
        }
        System.out.println("========================================================================");
    }

    /*
    public List<String> findPath1(String startId, String endId, boolean useDistance) {
        // Initialize distances, previous nodes, and priority queue
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousNode = new HashMap<>();
        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        // Set initial distances
        for (String locationId : locations.keySet()) {
            distances.put(locationId, Double.MAX_VALUE);
        }
        distances.put(startId, 0.0);
        priorityQueue.add(startId);

        while (!priorityQueue.isEmpty()) {
            String current = priorityQueue.poll();

            // Early exit if we reach the destination
            if (current.equals(endId)) {
                break;
            }

            // Update distances for neighbors
            for (Road road : getConnectedRoads(current)) {
                String neighbor = road.getDestination().getId();
                double cost = useDistance ? road.getDistance() : road.getTravelTime();
                double newDistance = distances.get(current) + cost;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNode.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }

        // Reconstruct the path
        List<String> path = new LinkedList<>();
        for (String at = endId; at != null; at = previousNode.get(at)) {
            path.add(0, at);
        }

        if (!path.isEmpty() && path.get(0).equals(startId)) {
            return path; // Valid path
        } else {
            return Collections.emptyList(); // No path found
        }
    }
*/


    public List<String> findPath(String startId, String endId, boolean useDistance) {
        if (!locations.containsKey(startId) || !locations.containsKey(endId)) {
            return new ArrayList<>();
        }

        // Initialize distances and previous nodes
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousNode = new HashMap<>();
        Set<String> unvisited = new HashSet<>(locations.keySet());

        // Set initial distances to infinity except start node
        for (String locationId : locations.keySet()) {
            distances.put(locationId, Double.MAX_VALUE);
        }
        distances.put(startId, 0.0);


        while (!unvisited.isEmpty()) {
            // Find closest unvisited node
            String current = findClosestNode(unvisited, distances);

            if (current == null || current.equals(endId)) {
                break;
            }

            unvisited.remove(current);

            // Update distances to all neighbors
            for (Road road : getConnectedRoads(current)) {
                String neighbor = road.getDestination().getId();
                if (!unvisited.contains(neighbor)) {
                    continue;
                }


                // Choose whether to use distance or time as cost
                double cost = useDistance ? road.getDistance() : road.getTravelTime();
                double newDistance = distances.get(current) + cost;

                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNode.put(neighbor, current);
                }
            }
        }

        // Build the path
        return buildPath(startId, endId, previousNode);
    }

    // Add this method to find alternative routes avoiding congested areas
    public List<String> findAdaptiveRoute(String startId, String endId, LocalDateTime time) {
        if (!locations.containsKey(startId) || !locations.containsKey(endId)) {
            return new ArrayList<>();
        }
        
        // Initialize data structures for Dijkstra's algorithm
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousNode = new HashMap<>();
        Set<String> unvisited = new HashSet<>(locations.keySet());
        
        // Set initial distances
        for (String locationId : locations.keySet()) {
            distances.put(locationId, Double.MAX_VALUE);
        }
        distances.put(startId, 0.0);

//        System.out.println("===========================> DEBUG  <===========================");
//        System.out.println("LocalDateTime: " + time );
//        System.out.println("================================================================\n");


        while (!unvisited.isEmpty()) {
            String current = findClosestNode(unvisited, distances);
            if (current == null || current.equals(endId)) break;
            
            unvisited.remove(current);
            
            // Check each neighbor
            for (Road road : getConnectedRoads(current)) {
                String neighbor = road.getDestination().getId();
                if (!unvisited.contains(neighbor)) continue;
                
                // Consider both current congestion and predicted congestion
                double predictedCongestion = congestionPredictor.predictCongestion(road, time);
                double adjustedCost = road.getDistance() * predictedCongestion;
                
                // If this is likely to be a bottleneck, increase the cost
                if (congestionPredictor.isLikelyBottleneck(road, time)) {
                    adjustedCost *= 1.5; // Penalty for potential bottlenecks
                }
                
                double newDistance = distances.get(current) + adjustedCost;
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNode.put(neighbor, current);
                }
            }
        }
        
        return buildPath(startId, endId, previousNode);
    }


    private String findClosestNode(Set<String> unvisited, Map<String, Double> distances) {
        String closest = null;
        double minDistance = Double.MAX_VALUE;

        for (String node : unvisited) {
            double distance = distances.get(node);
            if (distance < minDistance) {
                minDistance = distance;
                closest = node;
            }
        }
        return closest;
    }


    private List<String> buildPath(String startId, String endId, Map<String, String> previousNode) {
        List<String> path = new ArrayList<>();
        String current = endId;

        while (current != null) {
            path.add(0, current);
            if (current.equals(startId)) {
                return path;
            }
            current = previousNode.get(current);
        }

        return new ArrayList<>(); // Return empty list if no path found
    }


    public List<String> findOptimalDeliveryRoute(
            String startId,
            Map<String, Double> deliveryLoads,
            Map<String, Double> deadlines,
            double vehicleCapacity) {

        // Initialize route planning
        List<String> bestRoute = new ArrayList<>();
        double[] bestRouteCost = {Double.MAX_VALUE};
        List<String> deliveryLocations = new ArrayList<>(deliveryLoads.keySet());

        // Try different permutations of delivery locations
        System.out.println("\n\nStarting route optimization:");
        System.out.println("\tDelivery locations: " + deliveryLocations);
        System.out.println("\tVehicle capacity: " + vehicleCapacity);
        System.out.println("\tStarting from: " + startId + "\n");

        generatePermutations(deliveryLocations, 0, startId, vehicleCapacity,
                deliveryLoads, deadlines, bestRoute, bestRouteCost);

        if (bestRoute.isEmpty()) {
            System.out.println("No valid route found. Checking constraints:");
            // Check total load
            double totalLoad = deliveryLoads.values().stream().mapToDouble(Double::doubleValue).sum();
            System.out.println("Total load: " + totalLoad + " / Capacity: " + vehicleCapacity);

            // Check connectivity
            for (String location : deliveryLocations) {
                List<String> path = findPath(startId, location, false);
                System.out.println("Path to " + location + ": " + (path.isEmpty() ? "No path found" : "Path exists"));
            }
        }
        return bestRoute;
    }

    private void generatePermutations(
            List<String> locations,
            int start,
            String startId,
            double vehicleCapacity,
            Map<String, Double> deliveryLoads,
            Map<String, Double> deadlines,
            List<String> bestRoute,
            double[] bestRouteCost) {  // Changed to double[] from double

        if (start == locations.size()) {
            // Evaluate this permutation
            evaluateRoute(locations, startId, vehicleCapacity,
                    deliveryLoads, deadlines, bestRoute, bestRouteCost);
            return;
        }

        // Generate permutations
        for (int i = start; i < locations.size(); i++) {
            // Swap elements to create new permutation
            Collections.swap(locations, start, i);

            // Debug output to show permutation being tested
            System.out.println("Testing permutation: " + locations);

            generatePermutations(locations, start + 1, startId, vehicleCapacity,
                    deliveryLoads, deadlines, bestRoute, bestRouteCost);

            // Restore the array
            Collections.swap(locations, start, i);
        }
    }



    private void evaluateRoute(
            List<String> route,
            String startId,
            double vehicleCapacity,
            Map<String, Double> deliveryLoads,
            Map<String, Double> deadlines,
            List<String> bestRoute,
            double[] bestRouteCost) {

        double currentLoad = 0;
        double currentTime = 0;
        String currentLocation = startId;
        boolean isValidRoute = true;

        System.out.println("\nEvaluating route: " + route);

        // Check if route is feasible
        for (String nextLocation : route) {
            // Check capacity constraint
            currentLoad += deliveryLoads.get(nextLocation);
            if (currentLoad > vehicleCapacity) {
                System.out.println("Capacity exceeded at " + nextLocation +
                        ": " + currentLoad + " > " + vehicleCapacity);
                isValidRoute = false;
                break;
            }

            // Find fastest path to next location
            List<String> pathToNext = findPath(currentLocation, nextLocation, false);
            if (pathToNext.isEmpty()) {
                System.out.println("No path found from " + currentLocation + " to " + nextLocation);
                isValidRoute = false;
                break;
            }

            // Calculate time to reach next location
            double timeToNext = calculatePathTime(pathToNext);
            currentTime += timeToNext;

            // Check deadline constraint
            if (deadlines.containsKey(nextLocation) && currentTime > deadlines.get(nextLocation)) {
                System.out.println("Deadline missed at " + nextLocation +
                        ": " + currentTime + " > " + deadlines.get(nextLocation));
                isValidRoute = false;
                break;
            }

            System.out.println("Reached " + nextLocation +
                    " at time " + currentTime +
                    " with load " + currentLoad);
            currentLocation = nextLocation;
        }

        // Update best route if this route is valid and better than current best
        if (isValidRoute && currentTime < bestRouteCost[0]) {
            bestRouteCost[0] = currentTime;
            bestRoute.clear();
            bestRoute.addAll(route);
            System.out.println("New best route found: " + route +
                    " with total time: " + currentTime);
        }
    }

    public double calculatePathTime(List<String> path) {
        double totalTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String currentId = path.get(i);
            String nextId = path.get(i + 1);

            // Find the road connecting these locations
            for (Road road : getConnectedRoads(currentId)) {
                if (road.getDestination().getId().equals(nextId)) {
                    totalTime += road.getTravelTime(); // Includes traffic conditions
                    break;
                }
            }
        }
        return totalTime;
    }


}