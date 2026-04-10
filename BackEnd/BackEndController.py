from FileHandler import FileHandler
from Account import Account
import sys

'''
The overall back end program that handles the end of day work of
updating the master bank accounts file with any and all transactions
recorded in the transaction log

Input files: old_master.txt (old master file), merged_transactions.txt (transaction log)
Output files: new_master.txt (new master file) (note: new master file also serves as new current accounts file)
How to run: currently run as a python file from BackEndController, reliant on all files in the BackEnd folder
            can be changed in order to accept file input from the command line if nessacry in later phases
'''


# Main driver class that manages the overall flow of the daily back-end processing
class BackEndController:
    def __init__(self, old_master, transaction_log, new_master):
        self.old_master = old_master
        self.transaction_log = transaction_log
        self.new_master = new_master
        
        # Storing accounts in a dictionary allows for O(1) lookups by account number
        self.accounts = {}  
        self.transactions = []

    # Orchestrates the sequence: read inputs -> apply transactions -> write output
    def run(self):
        print("Starting daily processing...")
        
        self.accounts = FileHandler.read_master_file(self.old_master)
        print(f"Loaded {len(self.accounts)} accounts from master file.")
        
        self.transactions = FileHandler.read_transaction_file(self.transaction_log)
        print(f"Loaded {len(self.transactions)} transactions to process.")
        
        self.processTransactions()
        print("Transactions applied successfully.")
        
        FileHandler.write_new_master_file(self.new_master, self.accounts)
        print("New master file generated successfully.")
        
        print("Session complete.")
    
    # Iterates through the daily transactions and updates the corresponding accounts
    def processTransactions(self):
        for tx in self.transactions:
            # Handle account creation separately since the account doesn't exist in the dictionary yet
            if tx.code == "05":
                if tx.number not in self.accounts:
                    self.accounts[tx.number] = Account(tx.number, tx.name, "A", tx.amount, "NP")
                continue

            # Skip transactions for accounts that don't exist in the master file
            if tx.number not in self.accounts:
                continue
                
            account = self.accounts[tx.number]

            # Route the transaction to the correct account method based on the 2-digit code
            if tx.code in ["01", "02", "03"]:    # Withdrawal, Transfer, Paybill
                account.withdraw(tx.amount)
            elif tx.code == "04":                # Deposit
                account.deposit(tx.amount)
            elif tx.code == "06":                # Delete
                if account.balance == 0:
                    del self.accounts[tx.number]
            elif tx.code == "07":                # Disable
                account.status = "D"
            elif tx.code == "08":                # Change Plan
                account.plan = "SP" if account.plan == "NP" else "NP"

# Entry point for the back-end system
if __name__ == "__main__":
    if len(sys.argv) != 4:
        controller = BackEndController("old_master.txt", "merged_transactions.txt", "new_master.txt")
    else:
        old_master = sys.argv[1]
        merged_transactions = sys.argv[2]
        new_master = sys.argv[3]
        controller = BackEndController(old_master,merged_transactions,new_master)
    controller.run()