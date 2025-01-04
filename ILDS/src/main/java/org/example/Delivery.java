package org.example;

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class Delivery implements Comparable<Delivery> {
    private String id;
    private String destinationId;
    private double load;
    private LocalDateTime deadline;
    private double estimatedTime;
    private DeliveryPriority priority;

    public enum DeliveryPriority {
        LOW(1),
        MEDIUM(2),
        HIGH(3);

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

    public void printDelivery() {

        // Define the formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.print("ID: " + id + "  |  ");
        System.out.print("Destination: " + destinationId + "  |  ");
        System.out.print("Load: " + String.format("%.2f", load) + "  |  ");
        System.out.print("Deadline: " + deadline.format(formatter) + "  |  ");
        System.out.print("Estimated Time: " + String.format("%.2f", estimatedTime) + " hours  |  ");
        System.out.println("Priority: " + priority);
    }


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

