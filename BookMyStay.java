/**
 * BookMyStay
 *
 * Hotel Booking Management System
 *
 * Version 2.0
 *
 * This version introduces object-oriented domain modeling
 * using abstraction and inheritance to represent different
 * room types and their static availability.
 *
 * @author Daksh Chaudhary
 * @version 2.0
 */

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

// Concrete Room Types

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

public class BookMyStay {

    public static void main(String[] args) {

        displayWelcomeMessage();

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);

        System.out.println("---------------------------------------");
        System.out.println("Application terminated successfully.");
    }

    private static void displayWelcomeMessage() {
        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 2.0            ");
        System.out.println("=======================================");
    }
}