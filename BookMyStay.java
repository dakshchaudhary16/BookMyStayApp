/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 8.1
 *
 * This version introduces Booking History and Reporting
 * for operational visibility and audit tracking.
 *
 * @author Daksh Chaudhary
 * @version 8.1
 */

import java.util.*;

// --------------------- INVENTORY ---------------------

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
}

// --------------------- RESERVATION ---------------------

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | RoomID: " + roomId;
    }
}

// --------------------- QUEUE ---------------------

class BookingRequestQueue {

    private Queue<String[]> queue = new LinkedList<>();

    public void addRequest(String guest, String roomType) {
        queue.offer(new String[]{guest, roomType});
    }

    public String[] getNextRequest() {
        return queue.poll();
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

// --------------------- BOOKING HISTORY ---------------------

class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

// --------------------- REPORT SERVICE ---------------------

class BookingReportService {

    public void displayAllBookings(List<Reservation> reservations) {

        System.out.println("\n===== Booking History =====");

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void generateSummary(List<Reservation> reservations) {

        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : reservations) {
            countMap.put(r.getRoomType(),
                    countMap.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\n===== Booking Summary =====");
        for (String type : countMap.keySet()) {
            System.out.println(type + " -> " + countMap.get(type) + " bookings");
        }
    }
}

// --------------------- BOOKING SERVICE ---------------------

class BookingService {

    private int roomCounter = 1;
    private int reservationCounter = 1;

    public List<Reservation> processBookings(
            BookingRequestQueue queue,
            RoomInventory inventory,
            BookingHistory history) {

        List<Reservation> confirmed = new ArrayList<>();

        while (queue.hasRequests()) {

            String[] req = queue.getNextRequest();
            String guest = req[0];
            String roomType = req[1];

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = roomType.substring(0, 3).toUpperCase() + "-" + roomCounter++;
                String reservationId = "RES-" + reservationCounter++;

                inventory.decrementAvailability(roomType);

                Reservation reservation = new Reservation(
                        reservationId, guest, roomType, roomId);

                confirmed.add(reservation);
                history.addReservation(reservation);

                System.out.println("Booking Confirmed: " + reservation);

            } else {
                System.out.println("Booking Failed for " + guest);
            }
        }

        return confirmed;
    }
}

// --------------------- MAIN ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        displayWelcomeMessage();

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService();
        BookingReportService reportService = new BookingReportService();

        queue.addRequest("Amit", "Single Room");
        queue.addRequest("Riya", "Double Room");
        queue.addRequest("Karan", "Suite Room");

        bookingService.processBookings(queue, inventory, history);

        // Admin views history
        reportService.displayAllBookings(history.getAllReservations());

        // Admin views summary
        reportService.generateSummary(history.getAllReservations());

        System.out.println("\nApplication terminated successfully.");
    }

    private static void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 8.1            ");
        System.out.println("=======================================");
    }
}