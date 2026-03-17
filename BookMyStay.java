/**
 * BookMyStay
 *
 * This class represents the entry point of the
 * Book My Stay – Hotel Booking Management System.
 *
 * It demonstrates how a Java application begins execution
 * and prints a welcome message to the console.
 *
 * @author Daksh Chaudhary
 * @version 1.0
 */
public class BookMyStay {

    /**
     * Main method – Entry point of the application.
     * The JVM invokes this method to start execution.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        displayWelcomeMessage();

    }

    /**
     * Displays the application welcome message.
     */
    private static void displayWelcomeMessage() {

        System.out.println("=======================================");
        System.out.println("        Welcome to Book My Stay        ");
        System.out.println("     Hotel Booking Management System   ");
        System.out.println("                Version 1.0            ");
        System.out.println("=======================================");
        System.out.println("Application started successfully.");
    }
}