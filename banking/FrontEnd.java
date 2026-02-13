package banking;

import java.util.*;


public class FrontEnd {
    private static boolean loggedIn = false;
    private static String mode = ""; // admin or standard

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Bank System");

        while (input.hasNextLine()) {
            System.out.print("Enter command: ");
            String command = input.nextLine().trim().toLowerCase();

            // Routing
            if (command.equals("login")) {
                doLogin(input);
            } else if (command.equals("logout")) {
                doLogout();
            } else if (loggedIn) {
                // Use this as route to other transactions
                handleTransaction(command, input);
            } else {
                System.out.println("ERROR: Must login first.");
            }
        }
    }

    private static void doLogin(Scanner scanner) {
        System.out.print("Session type: ");
        mode = scanner.nextLine().trim().toLowerCase();
        loggedIn = true;
        System.out.println("Logged in as " + mode);
    }

    private static void handleTransaction(String cmd, Scanner scanner) {
        // Add logic for transfer, deposit and etc. here
        switch (cmd) {
            case "withdrawal":
                if (mode.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String withdrawAcc = scanner.nextLine();
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = Double.parseDouble(scanner.nextLine());

                if (mode.equals("standard") && withdrawAmount > 500.00) {
                    System.out.println("ERROR: Standard session withdrawal limit is $500.00");
                } else {
                    System.out.println("Withdrawal processed.");
                }
                break;
            case "create":
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
                System.out.println("Command recognized: " + cmd);
                break;
        }
    }

    private static void doLogout() {
        loggedIn = false;
        System.out.println("Logged out. Saving transaction file...");
    }
}