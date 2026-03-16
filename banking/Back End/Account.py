#note: assumes bank account has a plan from current bank accounts, readjust if not the case
# Class to store and retrieve account details and enforces service fee
class Account:
    # Parameterized constructor
    def __init__(self,account_number,name,status,balance,plan):
        self.account_number = account_number
        self.name = name
        self.status = status
        self.balance = balance
        self.plan = plan

    #method for applying fee, amount should be a postive number for positive transactions (deposit), and negative for negative transactions (eg withdrawal)
    def applyfee(self,amount):
        if(self.plan == "SP"):
            amount -= 0.10
        elif(self.plan == "NP"):
            amount -= 0.05
        else:
            print("ERROR: <Invalid account plan>")
        
        if(self.balance > abs(amount)):
            self.balance += amount
        else:
            print("ERROR: <transaction results in negative balance>")
        return