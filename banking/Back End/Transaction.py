# Class to store and retrieve details of a given transaction
class Transaction:
    # Parameterized constructor for a transaction
    def __init__(self,code,name,number,amount,misc):
        self.code = code
        self.name = name
        self.number = number
        self.amount = amount
        self.misc = misc