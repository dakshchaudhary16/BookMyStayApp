/**
 * BookMyStay
 * Version 11.1
 * UC11 - Concurrent Booking Simulation
 */

import java.util.*;

// --------------------- INVENTORY (THREAD SAFE) ---------------------

class RoomInventory {

    private Map<String, Integer> map = new HashMap<>();

    public RoomInventory() {
        map.put("Single Room", 2);
        map.put("Double Room", 1);
    }

    public synchronized boolean bookRoom(String type) {

        if (!map.containsKey(type)) {
            System.out.println(Thread.currentThread().getName() + " → Invalid Room");
            return false;
        }

        int available = map.get(type);

        if (available <= 0) {
            System.out.println(Thread.currentThread().getName() + " → No rooms available");
            return false;
        }

        // Critical section
        map.put(type, available - 1);

        System.out.println(Thread.currentThread().getName() +
                " → Booked " + type + " | Remaining: " + map.get(type));

        return true;
    }
}

// --------------------- RESERVATION ---------------------

class Reservation {

    String guest;
    String roomType;

    public Reservation(String g, String r) {
        guest = g;
        roomType = r;
    }
}

// --------------------- SHARED QUEUE ---------------------

class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void add(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation get() {
        return queue.poll();
    }
}

// --------------------- WORKER THREAD ---------------------

class BookingWorker extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingWorker(BookingQueue q, RoomInventory i, String name) {
        super(name);
        queue = q;
        inventory = i;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            synchronized (queue) {
                r = queue.get();
            }

            if (r == null)
                break;

            inventory.bookRoom(r.roomType);

            try {
                Thread.sleep(100); // simulate delay
            } catch (Exception e) {
            }
        }
    }
}

// --------------------- MAIN ---------------------

public class BookMyStay {

    public static void main(String[] args) {

        System.out.println("=== BookMyStay v11.1 (Concurrent Simulation) ===");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Multiple requests (simulate concurrency)
        queue.add(new Reservation("Amit", "Single Room"));
        queue.add(new Reservation("Riya", "Single Room"));
        queue.add(new Reservation("Karan", "Single Room")); // should fail

        // Multiple threads (guests)
        BookingWorker t1 = new BookingWorker(queue, inventory, "Thread-1");
        BookingWorker t2 = new BookingWorker(queue, inventory, "Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (Exception e) {
        }

        System.out.println("\nSystem completed without race conditions.");
    }
}