# Account.py

# Represents a single bank account and handles basic balance operations and fee deductions
class Account:
    def __init__(self, account_number, name, status, balance, plan, total_transactions=0):
        self.account_number = account_number
        self.name = name.strip()
        self.status = status
        self.balance = float(balance)
        self.plan = plan
        self.total_transactions = int(total_transactions)

    # Applies the specific fee based on the account's plan (SP or NP)
    def applyfee(self, amount):
        fee = 0.10 if self.plan == "SP" else 0.05
        
        # Deduct the fee whether money is entering or leaving the account
        if amount < 0:
            amount -= fee  
        else:
            amount -= fee  
        
        # Ensure the transaction doesn't overdraft the account
        if self.balance + amount >= 0:
            self.balance += amount
            self.total_transactions += 1
        else:
            print("Error: transaction results in negative balance")

    # Wrapper for deposits (positive amount)
    def deposit(self, amount):
        self.applyfee(abs(float(amount)))

    # Wrapper for withdrawals (negative amount)
    def withdraw(self, amount):
        self.applyfee(-abs(float(amount)))