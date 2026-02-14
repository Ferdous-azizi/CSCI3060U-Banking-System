package banking;

import java.io.*;
import java.util.*;


public class FrontEnd {
    private static boolean loggedIn = false;
    private static String sessionType = ""; // admin or standard
    private static List<String> accountList = new ArrayList<>(); //list of accounts
    private static List<String> transactionsList = new ArrayList<>(); //list of transactions
    private static String holderName; //name of account

    //main method, loops infinitely, only allows transactions when logged in
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

    //method to login
    private static void doLogin(Scanner scanner) {
        System.out.print("Session type: ");
        sessionType = scanner.nextLine().trim().toLowerCase();
        loggedIn = true;
        System.out.println("Logged in as " + sessionType);
        if(sessionType.equals("standard")){
            System.out.print("Enter account holder's name: ");
            holderName = scanner.nextLine();
        }
        //read in current bank accounts
        //readFile(new File("current_bank_accounts.txt")); adjust file path
    }

    //logic for handling each transaction
    //MISSING ACCOUNT VERIFICATION LOGIC AND CONSTRAINTS, TO BE DONE WHEN TESTING
    private static void handleTransaction(String cmd, Scanner scanner) {
        // Add logic for transfer, deposit and etc. here
        switch (cmd) {
            case "withdrawal":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String withdrawAcc = scanner.nextLine();
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = Double.parseDouble(scanner.nextLine());

                if (sessionType.equals("standard") && withdrawAmount > 500.00) {
                    System.out.println("ERROR: Standard session withdrawal limit is $500.00");
                } else {
                    recordTransaction("01",holderName,withdrawAcc,withdrawAmount,"00");
                    System.out.println("Withdrawal processed.");
                }
                break;
            case "create":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter new account holder's name: ");
                    String newAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    Double initBal = Double.parseDouble(scanner.nextLine());
                    recordTransaction("05",newAcc,accNum,initBal,"00");
                    System.out.println("Account creation recorded.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            // Start here
            case "deposit":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String depositAcc = scanner.nextLine();
                System.out.print("Enter amount to deposit: ");
                double depositAmount = Double.parseDouble(scanner.nextLine());
                //add account verification logic
                recordTransaction("04",holderName,depositAcc,depositAmount,"00");
                System.out.println("Deposit processed");
                break;
            case "transfer":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String fromAcc = scanner.nextLine();
                System.out.print("Enter account number to transfer to: ");
                String toAcc = scanner.nextLine();
                System.out.print("Enter amount to transfer: ");
                double transferAmount = Double.parseDouble(scanner.nextLine());
                if(sessionType.equals("standard") && transferAmount > 1000){
                    System.out.println("ERROR: Standard session transfer is limit 1000");
                }
                else{
                    recordTransaction("02",holderName,fromAcc,transferAmount,"00");
                    //record deposit for the account being transferred to?
                    System.out.println("Transfer processed");
                }
                break;
            case "paybill":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String numAcc = scanner.nextLine();
                System.out.print("Enter the company you wish to pay: ");
                String companyName = scanner.nextLine();
                System.out.print("Enter amount to pay: ");
                double paybillAmount = Double.parseDouble(scanner.nextLine());
                recordTransaction("03",holderName,numAcc,paybillAmount,"00");
                System.out.println("paybill processed");
                break;
            case "delete":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    String delAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String delAccNum = scanner.nextLine();
                    recordTransaction("06",delAcc,delAccNum,0.0,"00");
                    System.out.println("Account deletion recorded.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            case "disable":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    String disAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String disAccNum = scanner.nextLine();
                    recordTransaction("07",disAcc,disAccNum,0.0,"00");
                    System.out.println("Account disabled.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            case "changeplan":
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    String chplAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String chplNum = scanner.nextLine();
                    recordTransaction("08",chplAcc,chplNum,0.0,"00");
                    System.out.println("Account plan changed.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            default:
                System.out.println("Command recognized: " + cmd);
                break;
        }
    }

    //method to logout, writes transactionsList to bank_account_transactions
    private static void doLogout() {
        loggedIn = false;
        recordTransaction("00","N/A","0",0.0,"00");
        System.out.println("Logged out. Saving transaction file...");
        //writeFile(new File("bank_account_transactions")); adjust file path
    }

    //method for reading from current_bank_account, disabled due to lack of said file
    private static void readFile(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String account;
            while ((account = br.readLine()) != null) {
                accountList.add(account);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    //method for writing to bank_account_transactions, disabled due to lack of said file
    private static void writeFile(File file){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for(String transaction : transactionsList){
                bw.write(transaction);
                bw.newLine();
            }
        }
        catch(IOException e){
            System.out.println("Error writing file.");
        }
    }
    //method to format and record transactions in transactionList
    private static void recordTransaction(String tsCode,String holderName,String accNum, Double funds,String misc){
        while(holderName.length() != 20){
            holderName = holderName.concat("_");
        }
        holderName = holderName.replaceAll(" ","_");
        while (accNum.length() != 5){
            accNum = "0" + accNum;
        }

        String fundsStr = funds.toString();
        while(fundsStr.length() != 6){
            fundsStr = "0" + fundsStr;
        }
        fundsStr = fundsStr + "0";
        String transaction = tsCode + "_" + holderName + "_" + accNum + "_" + fundsStr + "_" + misc;
        System.out.println(transaction); //temp print statement for correctness
        transactionsList.add(transaction);
    }
}