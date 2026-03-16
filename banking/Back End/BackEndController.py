from FileHandler import FileHandler
from Transaction import Transaction
from Account import Account

#note: there are discrepensies between how the starter code treats input files, vs how the assignment instructions treat files
#note: wait for instructor feedback on how to treat input files
# Class to manage control flow of the Back End, coordinates file I/O and transaction processing
class backEndController:
    # default constuctor
    def __init__(self):
        self.accounts = []
        self.transactions = []

    # executes sequence of loading data, processing transactions, and saving results
    def run(self):
        #read master into accounts
        #read trans log into transactions
        #processTransactions 
        #write current accounts file
        #write transaction file
        pass
    
    # iterates through list of tranactions, applying appropriate logic to target accounts
    def processTransactions():
        #apply fee based on transaction to account (+ num if postive eg. deposit,-if num if negative eg. withdrawal)
        pass