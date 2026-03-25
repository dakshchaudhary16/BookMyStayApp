/**
 * BookMyStay
 * Version 12.1
 * UC12 - Data Persistence & Recovery
 */

import java.io.*;
import java.util.*;

// --------------------- INVENTORY ---------------------

class RoomInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, Integer> map = new HashMap<>();

    public RoomInventory() {
        map.put("Single Room", 2);
        map.put("Double Room", 1);
    }

    public int getAvailability(String type) {
        return map.getOrDefault(type, 0);
    }

    public void book(String type) {
        if (map.containsKey(type) && map.get(type) > 0) {
            map.put(type, map.get(type) - 1);
        }
    }

    public void display() {
        System.out.println("Inventory: " + map);
    }
}

// --------------------- RESERVATION ---------------------

class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;
    String guest;
    String roomType;

    public Reservation(String id, String guest, String roomType) {
        this.id = id;
        this.guest = guest;
        this.roomType = roomType;
    }

    public String toString() {
        return id + " | " + guest + " | " + roomType;
    }
}

// --------------------- HISTORY ---------------------

class BookingHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    List<Reservation> list = new ArrayList<>();

    public void add(Reservation r) {
        list.add(r);
    }

    public void show() {
        System.out.println("\nHistory:");
        for (Reservation r : list) {
            System.out.println(r);
        }
    }
}

// --------------------- PERSISTENCE SERVICE ---------------------

class PersistenceService {

    private static final String FILE_NAME = "bookmystay.dat";

    // Save data
    public static void save(RoomInventory inv, BookingHistory history) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(inv);
            out.writeObject(history);

            System.out.println("\nData saved successfully.");

        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load data
    public static Object[] load() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inv = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();

            System.out.println("Data loaded successfully.");
            return new Object[]{inv, history};

        } catch (Exception e) {

            System.out.println("No previous data found. Starting fresh.");
            return null;
        }
    }
}

// --------------------- MAIN ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=== BookMyStay v12.1 (Persistence) ===");

        RoomInventory inventory;
        BookingHistory history;

        // Load previous state
        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        // Simulate booking
        String id = "RES-" + (history.list.size() + 1);

        if (inventory.getAvailability("Single Room") > 0) {

            inventory.book("Single Room");

            Reservation r = new Reservation(id, "Amit", "Single Room");
            history.add(r);

            System.out.println("Booking Done → " + r);
        } else {
            System.out.println("No rooms available.");
        }

        inventory.display();
        history.show();

        // Save state before exit
        PersistenceService.save(inventory, history);

        System.out.println("\nSystem shutdown complete.");
    }
}