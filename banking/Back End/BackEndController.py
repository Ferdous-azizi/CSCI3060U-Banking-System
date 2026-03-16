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
    def processTransactions(self):
        for transaction in self.transactions:
            # subtractive transactions (withdrawl, transfer, paybill)
            if transaction.code == "01" or transaction.code == "02" or transaction.code == "03":
                for account in self.accounts:
                    if transaction.name == account.name and transaction.number == account.number:
                        account.balance -= transaction.amount
            # deposit transaction
            elif transaction.code == "04":
                for account in self.accounts:
                    if transaction.name == account.name and transaction.number == account.number:
                        account.balance += transaction.amount
            # create transaction (currently always sets plan to non-student)
            elif transaction.code == "05":
                self.accounts.append(Account(transaction.number, transaction.name, "A", transaction.amount, "NP"))
            # delete transaction (waiting on account list implementation)
            elif transaction.code == "06":
                for account in self.accounts:
                    if transaction.name == account.name and transaction.number == account.number:
                        # self.accounts.remove(transaction.number)
                        pass
            # disable transaction (currently only sets to disabled)
            elif transaction.code == "07":
                for account in self.accounts:
                    if transaction.name == account.name and transaction.number == account.number:
                        account.status = "D"
            # changeplan transaction
            elif transaction.code == "08":
                for account in self.accounts:
                    if transaction.name == account.name and transaction.number == account.number:
                        if account.plan == "NP":
                            account.plan = "SP"
                        else:
                            account.plan = "NP"