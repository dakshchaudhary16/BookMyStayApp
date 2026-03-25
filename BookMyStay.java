/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 7.1
 *
 * UC1 → UC7 implemented
 * Includes Add-On Service Selection without modifying booking logic
 *
 * @author Daksh
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

    private String guestName;
    private String roomType;
    private String reservationId;
    private String roomId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getReservationId() { return reservationId; }

    public void confirm(String reservationId, String roomId) {
        this.reservationId = reservationId;
        this.roomId = roomId;
    }
}

// --------------------- QUEUE ---------------------

class BookingRequestQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNext() {
        return queue.poll();
    }

    public boolean hasRequests() {
        return !queue.isEmpty();
    }
}

// --------------------- BOOKING SERVICE ---------------------

class BookingService {

    private int roomCounter = 1;
    private int reservationCounter = 1;

    public List<Reservation> process(BookingRequestQueue queue, RoomInventory inventory) {

        List<Reservation> confirmed = new ArrayList<>();

        while (queue.hasRequests()) {

            Reservation r = queue.getNext();

            if (inventory.getAvailability(r.getRoomType()) > 0) {

                String roomId = r.getRoomType().substring(0,3).toUpperCase() + "-" + roomCounter++;
                String reservationId = "RES-" + reservationCounter++;

                inventory.decrementAvailability(r.getRoomType());

                r.confirm(reservationId, roomId);
                confirmed.add(r);

                System.out.println("Booking Confirmed → " + reservationId + " | " + r.getGuestName());

            } else {
                System.out.println("Booking Failed for " + r.getGuestName());
            }
        }

        return confirmed;
    }
}

// --------------------- ADD-ON SERVICE ---------------------

class AddOnService {

    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() { return name; }
    public double getCost() { return cost; }
}

// --------------------- ADD-ON MANAGER ---------------------

class AddOnServiceManager {

    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added → " + service.getName());
    }

    public void displayServices(String reservationId) {

        System.out.println("\nServices for " + reservationId);

        List<AddOnService> list = serviceMap.get(reservationId);

        if (list == null) {
            System.out.println("No services added.");
            return;
        }

        for (AddOnService s : list) {
            System.out.println(s.getName() + " ₹" + s.getCost());
        }
    }

    public double calculateTotal(String reservationId) {

        double total = 0;

        List<AddOnService> list = serviceMap.get(reservationId);

        if (list != null) {
            for (AddOnService s : list) {
                total += s.getCost();
            }
        }

        return total;
    }
}

// --------------------- MAIN ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=== Book My Stay (Version 7.1) ===");

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService();
        AddOnServiceManager manager = new AddOnServiceManager();

        // Booking Requests
        queue.addRequest(new Reservation("Amit", "Single Room"));
        queue.addRequest(new Reservation("Riya", "Double Room"));
        queue.addRequest(new Reservation("Karan", "Single Room"));

        List<Reservation> confirmed = bookingService.process(queue, inventory);

        // Add-on services
        if (!confirmed.isEmpty()) {

            String resId = confirmed.get(0).getReservationId();

            manager.addService(resId, new AddOnService("Breakfast", 500));
            manager.addService(resId, new AddOnService("Pickup", 1200));

            manager.displayServices(resId);

            System.out.println("Total Add-On Cost: ₹" + manager.calculateTotal(resId));
        }

        System.out.println("\nDone.");
    }
}