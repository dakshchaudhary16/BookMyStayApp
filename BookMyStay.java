/**
 * BookMyStay
 * Version 9.1
 * UC1 → UC9 (Final)
 */

import java.util.*;

// --------------------- CUSTOM EXCEPTION ---------------------

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// --------------------- INVENTORY ---------------------

class RoomInventory {

    private Map<String, Integer> map;

    public RoomInventory() {
        map = new HashMap<>();
        map.put("Single Room", 2);
        map.put("Double Room", 1);
        map.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return map.getOrDefault(type, 0);
    }

    public boolean isValidRoomType(String type) {
        return map.containsKey(type);
    }

    public void decrement(String type) throws InvalidBookingException {

        if (!isValidRoomType(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }

        if (map.get(type) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + type);
        }

        map.put(type, map.get(type) - 1);
    }
}

// --------------------- RESERVATION ---------------------

class Reservation {

    private String guest;
    private String roomType;
    private String reservationId;

    public Reservation(String guest, String roomType) {
        this.guest = guest;
        this.roomType = roomType;
    }

    public String getGuest() { return guest; }
    public String getRoomType() { return roomType; }

    public void confirm(String id) {
        this.reservationId = id;
    }

    public String getReservationId() {
        return reservationId;
    }
}

// --------------------- QUEUE ---------------------

class BookingQueue {

    private Queue<Reservation> q = new LinkedList<>();

    public void add(Reservation r) {
        q.offer(r);
    }

    public Reservation next() {
        return q.poll();
    }

    public boolean has() {
        return !q.isEmpty();
    }
}

// --------------------- VALIDATOR ---------------------

class BookingValidator {

    public static void validate(Reservation r, RoomInventory inv)
            throws InvalidBookingException {

        if (r.getGuest() == null || r.getGuest().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (!inv.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }
    }
}

// --------------------- BOOKING SERVICE ---------------------

class BookingService {

    private int counter = 1;

    public List<Reservation> process(BookingQueue q, RoomInventory inv) {

        List<Reservation> list = new ArrayList<>();

        while (q.has()) {

            Reservation r = q.next();

            try {
                BookingValidator.validate(r, inv);

                inv.decrement(r.getRoomType());

                String id = "RES-" + counter++;
                r.confirm(id);

                list.add(r);

                System.out.println("Booking Success → " + id + " | " + r.getGuest());

            } catch (InvalidBookingException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        return list;
    }
}

// --------------------- ADD-ON SERVICE ---------------------

class AddOnService {

    String name;
    double cost;

    public AddOnService(String n, double c) {
        name = n;
        cost = c;
    }
}

// --------------------- ADD-ON MANAGER ---------------------

class AddOnManager {

    Map<String, List<AddOnService>> map = new HashMap<>();

    public void add(String resId, AddOnService s) {
        map.computeIfAbsent(resId, k -> new ArrayList<>()).add(s);
    }

    public double total(String resId) {

        double t = 0;

        if (map.containsKey(resId)) {
            for (AddOnService s : map.get(resId)) {
                t += s.cost;
            }
        }

        return t;
    }
}

// --------------------- HISTORY ---------------------

class BookingHistory {

    List<Reservation> list = new ArrayList<>();

    public void add(Reservation r) {
        list.add(r);
    }

    public List<Reservation> getAll() {
        return list;
    }
}

// --------------------- REPORT ---------------------

class ReportService {

    public void show(List<Reservation> list) {

        System.out.println("\nBooking History:");

        for (Reservation r : list) {
            System.out.println(r.getReservationId() + " | " + r.getGuest());
        }
    }
}

// --------------------- MAIN ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=== BookMyStay v9.1 ===");

        RoomInventory inv = new RoomInventory();
        BookingQueue q = new BookingQueue();
        BookingService service = new BookingService();
        BookingHistory history = new BookingHistory();
        AddOnManager addOn = new AddOnManager();
        ReportService report = new ReportService();

        // Test cases (including invalid)
        q.add(new Reservation("Amit", "Single Room"));
        q.add(new Reservation("", "Double Room")); // invalid
        q.add(new Reservation("Riya", "Invalid Room")); // invalid

        List<Reservation> confirmed = service.process(q, inv);

        for (Reservation r : confirmed) {
            history.add(r);
        }

        if (!confirmed.isEmpty()) {

            String id = confirmed.get(0).getReservationId();

            addOn.add(id, new AddOnService("Breakfast", 500));
            addOn.add(id, new AddOnService("Pickup", 1000));

            System.out.println("Add-on total: ₹" + addOn.total(id));
        }

        report.show(history.getAll());

        System.out.println("\nSystem running safely after errors.");
    }
}