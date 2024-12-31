package org.example;

import java.util.*;
import java.time.*;


public class Delivery implements Comparable<Delivery> {
    private String id;
    private String destinationId;
    private double load;
    private LocalDateTime deadline;
    private double estimatedTime;
    private DeliveryPriority priority;

    public enum DeliveryPriority {
        HIGH(3),
        MEDIUM(2),
        LOW(1);

        private final int value;
        DeliveryPriority(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    public Delivery(String id, String destinationId, double load,
                    LocalDateTime deadline, double estimatedTime,
                    DeliveryPriority priority) {
        this.id = id;
        this.destinationId = destinationId;
        this.load = load;
        this.deadline = deadline;
        this.estimatedTime = estimatedTime;
        this.priority = priority;
    }

    // Getters
    public String getId() { return id; }
    public String getDestinationId() { return destinationId; }
    public double getLoad() { return load; }
    public LocalDateTime getDeadline() { return deadline; }
    public double getEstimatedTime() { return estimatedTime; }
    public DeliveryPriority getPriority() { return priority; }

    @Override
    public int compareTo(Delivery other) {
        // First compare by priority
        int priorityCompare = Integer.compare(
                other.priority.getValue(),
                this.priority.getValue()
        );
        if (priorityCompare != 0) return priorityCompare;

        // Then by deadline
        return this.deadline.compareTo(other.deadline);
    }
}

