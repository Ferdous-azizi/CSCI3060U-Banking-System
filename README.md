# CSCI3060U-Banking-System

How to Run the Back End
1. Navigate to the Directory
Open your terminal and cd into the backend folder: cd BackEnd

2. Prepare Your Files
Ensure the following files are present in this directory:
*old_master.txt: The current state of all bank accounts (45-character format). 
*merged_transactions.txt: The summary file containing all transactions from the frontend. 

3. Run the Controller
Execute the main program to start the processing sequence: python BackEndController.py
4. Results
Once the script finishes, a file named new_master.txt will be created in the same folder. This file contains the updated account balances and statuses after applying the day's transactions.
