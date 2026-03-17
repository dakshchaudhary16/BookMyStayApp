/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 5.1
 *
 * This version introduces a Booking Request Queue
 * using FIFO (First-Come-First-Served) principle
 * to fairly manage multiple booking requests.
 *
 * No inventory mutation occurs at this stage.
 *
 * @author Daksh Chaudhary
 * @version 5.1
 */

import java.util.*;

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

    public void displayRoomDetails() {
        System.out.println("---------------------------------------");
        System.out.println("Room Type      : " + roomType);
        System.out.println("Beds           : " + numberOfBeds);
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

// --------------------- INVENTORY (Read-Only Here) ---------------------

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 3);
        availabilityMap.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }
}

// --------------------- RESERVATION MODEL ---------------------

class Reservation {

    private String guestName;
    private String requestedRoomType;

    public Reservation(String guestName, String requestedRoomType) {
        this.guestName = guestName;
        this.requestedRoomType = requestedRoomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRequestedRoomType() {
        return requestedRoomType;
    }

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Requested: " + requestedRoomType;
    }
}

// --------------------- BOOKING REQUEST QUEUE ---------------------

class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add request (FIFO order preserved automatically)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added: " + reservation);
    }

    // View all pending requests (no allocation)
    public void displayPendingRequests() {
        System.out.println("\n===== Pending Booking Requests (FIFO Order) =====");
        for (Reservation reservation : requestQueue) {
            System.out.println(reservation);
        }
    }
}

// --------------------- APPLICATION ENTRY ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        displayWelcomeMessage();

        // Initialize inventory (no mutation in this use case)
        RoomInventory inventory = new RoomInventory();

        // Initialize booking request queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulate guest booking requests
        bookingQueue.addRequest(new Reservation("Amit", "Single Room"));
        bookingQueue.addRequest(new Reservation("Riya", "Double Room"));
        bookingQueue.addRequest(new Reservation("Karan", "Suite Room"));

        // Display queued requests
        bookingQueue.displayPendingRequests();

        System.out.println("\nNo allocation performed in this stage.");
        System.out.println("Application terminated successfully.");
    }

    private static void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 5.1            ");
        System.out.println("=======================================");
    }
}