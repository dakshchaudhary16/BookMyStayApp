/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 3.1
 *
 * This version introduces centralized room inventory
 * management using HashMap to maintain a single source
 * of truth for room availability.
 *
 * @author Daksh Chaudhary
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// --------------------- ROOM DOMAIN MODEL ---------------------

abstract class Room {

    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("---------------------------------------");
        System.out.println("Room Type      : " + roomType);
        System.out.println("Number of Beds : " + numberOfBeds);
        System.out.println("Price/Night    : ₹" + pricePerNight);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }
}

// --------------------- INVENTORY MANAGEMENT ---------------------

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();

        // Initialize room availability
        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 3);
        availabilityMap.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int newCount) {
        availabilityMap.put(roomType, newCount);
    }

    public void displayInventory() {
        System.out.println("========== Current Room Inventory ==========");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// --------------------- APPLICATION ENTRY ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        displayWelcomeMessage();

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Display room details + availability
        single.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(single.getRoomType()));

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getRoomType()));

        suite.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(suite.getRoomType()));

        System.out.println("---------------------------------------");

        // Display entire inventory
        inventory.displayInventory();

        System.out.println("---------------------------------------");
        System.out.println("Application terminated successfully.");
    }

    private static void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 3.1            ");
        System.out.println("=======================================");
    }
}