package banking;

import java.util.*;


/**
 * The FrontEnd class represents the user interface for the banking system.
 * It handles user input, session management (login/logout), and routes commands
 * to appropriate transaction handlers based on the user's session type.
 */
public class FrontEnd {
    /** Tracks whether a user is currently logged into the system */
    private static boolean loggedIn = false;
    
    /** Stores the current session type - either "admin" or "standard" */
    private static String mode = ""; // admin or standard

    /**
     * The main entry point for the banking system application.
     * Continuously reads user commands from standard input and routes them
     * to the appropriate handlers based on login status and session type.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Bank System");

        // Main program loop - continues until input stream is closed
        while (input.hasNextLine()) {
            System.out.print("Enter command: ");
            String command = input.nextLine().trim().toLowerCase();

            // Route commands based on login status and command type
            if (command.equals("login")) {
                doLogin(input);
            } else if (command.equals("logout")) {
                doLogout();
            } else if (loggedIn) {
                // Process transaction commands for logged-in users
                handleTransaction(command, input);
            } else {
                System.out.println("ERROR: Must login first.");
            }
        }
    }

    /**
     * Handles the login process for the banking system.
     * Prompts the user for session type and sets the login status.
     *
     * @param scanner The Scanner object used to read user input
     */
    private static void doLogin(Scanner scanner) {
        System.out.print("Session type: ");
        mode = scanner.nextLine().trim().toLowerCase();
        loggedIn = true;
        System.out.println("Logged in as " + mode);
    }

    /**
     * Routes transaction commands to their specific handlers based on the
     * command type and user's session mode.
     *
     * @param cmd     The transaction command entered by the user
     * @param scanner The Scanner object used to read additional input
     */
    private static void handleTransaction(String cmd, Scanner scanner) {
        // Add logic for transfer, deposit and etc. here
        switch (cmd) {
            case "withdrawal":
                // Handle withdrawal transaction
                if (mode.equals("admin")) {
                    // Admin withdrawals require account holder's name
                    System.out.print("Enter account holder's name: ");
                    scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String withdrawAcc = scanner.nextLine();
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = Double.parseDouble(scanner.nextLine());

                // Standard users have a $500 withdrawal limit
                if (mode.equals("standard") && withdrawAmount > 500.00) {
                    System.out.println("ERROR: Standard session withdrawal limit is $500.00");
                } else {
                    System.out.println("Withdrawal processed.");
                }
                break;
                
            case "create":
                // Handle account creation - admin only transaction
                if (mode.equals("admin")) {
                    System.out.print("Enter new account holder's name: ");
                    scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    scanner.nextLine();
                    System.out.println("Account creation recorded.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
                
            // Start here
            default:
                // Placeholder for unimplemented commands
                System.out.println("Command recognized: " + cmd);
                break;
        }
    }

    /**
     * Handles the logout process for the banking system.
     * Clears login status and simulates saving the transaction file.
     */
    private static void doLogout() {
        loggedIn = false;
        System.out.println("Logged out. Saving transaction file...");
    }
}
