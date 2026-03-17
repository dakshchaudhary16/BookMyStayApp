/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 6.1
 *
 * This version introduces Reservation Confirmation
 * and Safe Room Allocation with double-booking prevention.
 *
 * @author Daksh Chaudhary
 * @version 6.1
 */

import java.util.*;

// --------------------- INVENTORY SERVICE ---------------------

class RoomInventory {

    private Map<String, Integer> availabilityMap;

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 2);
        availabilityMap.put("Double Room", 1);
        availabilityMap.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        availabilityMap.put(roomType, availabilityMap.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n===== Updated Inventory =====");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
        }
    }
}

// --------------------- RESERVATION ---------------------

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
}

// --------------------- BOOKING REQUEST QUEUE ---------------------

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

// --------------------- BOOKING SERVICE ---------------------

class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocationMap = new HashMap<>();
    private int roomCounter = 1;

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        while (queue.hasRequests()) {

            Reservation reservation = queue.getNextRequest();
            String roomType = reservation.getRequestedRoomType();

            System.out.println("\nProcessing booking for: " + reservation.getGuestName());

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = generateUniqueRoomId(roomType);

                allocatedRoomIds.add(roomId);

                roomAllocationMap
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrementAvailability(roomType);

                System.out.println("Booking Confirmed!");
                System.out.println("Guest: " + reservation.getGuestName());
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking Failed - No rooms available for " + roomType);
            }
        }
    }

    private String generateUniqueRoomId(String roomType) {

        String prefix = roomType.replace(" ", "").substring(0, 3).toUpperCase();
        String roomId;

        do {
            roomId = prefix + "-" + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }
}

// --------------------- APPLICATION ENTRY ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        displayWelcomeMessage();

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService();

        // Add booking requests (FIFO order)
        queue.addRequest(new Reservation("Amit", "Single Room"));
        queue.addRequest(new Reservation("Riya", "Single Room"));
        queue.addRequest(new Reservation("Karan", "Single Room")); // should fail

        // Process allocation
        bookingService.processBookings(queue, inventory);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated successfully.");
    }

    private static void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 6.1            ");
        System.out.println("=======================================");
    }
}