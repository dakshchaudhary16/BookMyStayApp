/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 4.1
 *
 * This version introduces Room Search functionality
 * with read-only access to centralized inventory.
 *
 * Search operations do NOT modify system state.
 *
 * @author Daksh Chaudhary
 * @version 4.1
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
        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 3);
        availabilityMap.put("Suite Room", 0); // intentionally 0 to test filtering
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public void displayInventory() {
        System.out.println("========== Current Room Inventory ==========");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// --------------------- SEARCH SERVICE (READ-ONLY) ---------------------

class RoomSearchService {

    public void searchAvailableRooms(Room[] rooms, RoomInventory inventory) {

        System.out.println("\n========== Available Rooms ==========");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive check: show only rooms with availability > 0
            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available Rooms: " + available);
            }
        }

        System.out.println("======================================");
    }
}

// --------------------- APPLICATION ENTRY ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        displayWelcomeMessage();

        // Initialize room domain objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = { single, doubleRoom, suite };

        // Centralized inventory
        RoomInventory inventory = new RoomInventory();

        // Search Service (Read-Only Access)
        RoomSearchService searchService = new RoomSearchService();

        // Guest initiates search
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println("Application terminated successfully.");
    }

    private static void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 4.1            ");
        System.out.println("=======================================");
    }
}