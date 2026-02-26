/*
The intent of this program is to create a front-end for a banking transaction application that is capable of handling all types of input without failure.
The inputs of this program are designed to be passed from the command-line, with the file of accounts & the transaction log file both being passed as arguments
from the command line. User input is for the terminal is required as well.
The outputs of this program are the standard output from the terminal as well as the transaction log file. 
NOTE: CURRENTLY CHANGES TO ACCOUNTS ARE NOT SAVED TO THE INPUT ACCOUNT FILE, TO BE DECIDED IF IT SHOULD
TO RUN: 
    From project ROOT:
    javac banking/FrontEnd.java
    java banking.FrontEnd banking\current_bank_accounts.txt banking\bank_account_transactions.txt
*/
package banking;

import java.io.*;
import java.util.*;


public class FrontEnd {
    private static boolean loggedIn = false; //flag for whether or not transactions can be accessed
    private static String sessionType = ""; // admin or standard
    private static List<String> accountList = new ArrayList<>(); //list of accounts
    private static List<String> transactionsList = new ArrayList<>(); //list of transactions
    private static String holderName; //name of account
    private static File accountsFile;
    private static File transactionFile;

    //main method, loops infinitely, only allows transactions when logged in
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Please input with the current list of bank accounts and transaction log files as inputs");
        }

        accountsFile = new File(args[0]);
        transactionFile = new File(args[1]);

        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Bank System");

        while (input.hasNextLine()) {
            System.out.print("Enter command: ");
            String command = input.nextLine().trim().toLowerCase();

            // Routing
            if (command.equals("login")) {
                //read in current bank accounts
                readFile(accountsFile);
                doLogin(input);
            } else if (command.equals("logout")) {
                doLogout();
                writeFile(transactionFile);
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
    }

    //logic for handling each transaction
    //SWITCH STATEMENT FOR SIMPLE, CLEAR PROTOTYPE CODE, CAN BE ADJUSTED TO BE CLOSER TO DESIGN DOCUMENT
    private static void handleTransaction(String cmd, Scanner scanner) {
        // Add logic for transfer, deposit and etc. here
        switch (cmd) {
            case "withdrawal":
                //method to withdraw from an account
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String withdrawAcc = scanner.nextLine();
                if(!validateAccount(withdrawAcc, holderName)){
                    System.out.println("ERROR: INVALID ACCOUNT");
                    break;
                }
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = Double.parseDouble(scanner.nextLine());
                if(!validateFundLoss(withdrawAcc, withdrawAmount)){
                    System.out.println("ERROR: WITHDRAWAL EXCEEDS CURRENT BALANCE");
                    break;
                }

                if (sessionType.equals("standard") && withdrawAmount > 500.00) {
                    System.out.println("ERROR: Standard session withdrawal limit is $500.00");
                } else {
                    recordTransaction("01",holderName,withdrawAcc,withdrawAmount,"00");
                    System.out.println("Withdrawal processed.");
                }
                break;
            case "create":
                //method to create a new account, admin only
                if (sessionType.equals("admin")) {
                    System.out.print("Enter new account holder's name: ");
                    String newAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String accNum = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    Double initBal = Double.parseDouble(scanner.nextLine());
                    //create NEW ACC
                    if(accNum.length() > 5 || newAcc.length() > 20){
                        System.out.println("Invalid information provided");
                    }
                    accountList.removeLast();
                    while(newAcc.length() < 20){
                        newAcc = newAcc + "_";
                    }
                    newAcc = newAcc.replace(" ", "_");
                    while(accNum.length() < 5){
                        accNum = "0" + accNum;
                    }
                    accountList.add(newAcc + "_" + accNum + "_A_" + initBal);
                    accountList.add("00000_End_Of_File_________D_00000000");

                    recordTransaction("05",newAcc,accNum,initBal,"00");
                    System.out.println("Account creation recorded.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            case "deposit":
                //method to deposit into an account
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String depositAcc = scanner.nextLine();
                if(!validateAccount(depositAcc, holderName)){
                    System.out.println("ERROR: INVALID ACCOUNT");
                    break;
                }
                System.out.print("Enter amount to deposit: ");
                double depositAmount = Double.parseDouble(scanner.nextLine());
                recordTransaction("04",holderName,depositAcc,depositAmount,"00");
                System.out.println("Deposit processed");
                break;
            case "transfer":
                //method to transfer from one account to another
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String fromAcc = scanner.nextLine();
                if(!validateAccount(fromAcc, holderName)){
                    System.out.println("ERROR: INVALID ACCOUNT");
                    break;
                }
                System.out.print("Enter account number to transfer to: ");
                String toAcc = scanner.nextLine();
                if(!validateAccountNoName(toAcc)){
                    System.out.println("ERROR: INVALID ACCOUNT");
                    break;
                }
                System.out.print("Enter amount to transfer: ");
                double transferAmount = Double.parseDouble(scanner.nextLine());
                if(!validateFundLoss(fromAcc, transferAmount)){
                    System.out.println("ERROR: WITHDRAWAL EXCEEDS CURRENT BALANCE");
                    break;
                }
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
                //method to pay a bill to company
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    holderName = scanner.nextLine();
                }
                System.out.print("Enter account number: ");
                String numAcc = scanner.nextLine();
                if(!validateAccount(numAcc, holderName)){
                    System.out.println("ERROR: INVALID ACCOUNT");
                    break;
                }
                System.out.print("Enter the company you wish to pay: ");
                String companyName = scanner.nextLine();
                if(!companyName.equals("EC") && !companyName.equals("CQ") && !companyName.equals("FI")){
                    System.out.println("ERROR: INVALID COMPANY NAME");
                    break;
                }
                System.out.print("Enter amount to pay: ");
                double paybillAmount = Double.parseDouble(scanner.nextLine());
                if(!validateFundLoss(numAcc, paybillAmount)){
                    System.out.println("ERROR: WITHDRAWAL EXCEEDS CURRENT BALANCE");
                    break;
                }
                recordTransaction("03",holderName,numAcc,paybillAmount,"00");
                System.out.println("paybill processed");
                break;
            case "delete":
                //method to delete an account, admin only
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    String delAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String delAccNum = scanner.nextLine();
                    if(!validateAccount(delAccNum, delAcc)){
                        System.out.println("ERROR: INVALID ACCOUNT");
                        break;
                    }
                    //Delete ACC
                    while(delAccNum.length() < 5){
                        delAccNum = "0" + delAccNum;
                    }
                    for(int i = 0; i < accountList.size(); i++){
                        if(accountList.get(i).substring(0,5).equals(delAccNum)){
                            accountList.remove(i);
                            break;
                        }
                    }
                    recordTransaction("06",delAcc,delAccNum,0.0,"00");
                    System.out.println("Account deletion recorded.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            case "disable":
                //method to disable an account, admin only
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    String disAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String disAccNum = scanner.nextLine();
                    if(!validateAccount(disAccNum,disAcc)){
                        System.out.println("ERROR: INVALID ACCOUNT");
                        break;
                    }
                    //DISABLE ACC
                    while(disAccNum.length() < 5){
                        disAccNum = "0" + disAccNum;
                    }
                    for(int i = 0; i < accountList.size(); i++){
                        if(accountList.get(i).substring(0,5).equals(disAccNum)){
                            String temp = accountList.get(i);
                            accountList.remove(i);
                            temp = temp.substring(0,26) + "D" + temp.substring(27);
                            System.out.println(temp);
                            accountList.add(i, temp);
                            break;
                        }
                    }
                    recordTransaction("07",disAcc,disAccNum,0.0,"00");
                    System.out.println("Account disabled.");
                } else {
                    System.out.println("ERROR: Admin only.");
                }
                break;
            case "changeplan":
                //method to change account plan, admin only
                if (sessionType.equals("admin")) {
                    System.out.print("Enter account holder's name: ");
                    String chplAcc = scanner.nextLine();
                    System.out.print("Enter account number: ");
                    String chplNum = scanner.nextLine();
                    if(!validateAccount(chplNum,chplAcc)){
                        System.out.println("ERROR: INVALID ACCOUNT");
                        break;
                    }
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
    private static boolean validateAccount(String accNum,String holderName){
        if(holderName.length() > 20){
            return false;
        }
        while(holderName.length() != 20){
            holderName = holderName.concat("_");
        }
        holderName = holderName.replaceAll(" ","_");
        if(accNum.length() > 5){
            return false;
        }
        while (accNum.length() != 5){
            accNum = "0" + accNum;
        }
        for(String account : accountList){
            if(accNum.equals(account.substring(0, 5)) && holderName.equals(account.substring(6,26)) 
            && account.charAt(26) == 'A'){
                return true;
            }
        }
        return false;
    }
    private static boolean validateFundLoss(String accNum,Double moneyToLose){
        String moneyInAccount = "0.0";
        if(accNum.length() > 5){
            return false;
        }
        while (accNum.length() != 5){
            accNum = "0" + accNum;
        }
        for(String account : accountList){
            if(accNum.equals(account.substring(0,5))){
                moneyInAccount = account.substring(29);
            }
        }
        if((Double.parseDouble(moneyInAccount) - moneyToLose) > 0){
            return true;
        }
        else{
            return false;
        }
    }
    private static boolean validateAccountNoName(String accNum){
        if(accNum.length() > 5){
            return false;
        }
        while (accNum.length() != 5){
            accNum = "0" + accNum;
        }
        for(String account : accountList){
            if(accNum.equals(account.substring(0,5)) && account.charAt(26) == 'A'){
                return true;
            }
        }
        return false;
    }
}
