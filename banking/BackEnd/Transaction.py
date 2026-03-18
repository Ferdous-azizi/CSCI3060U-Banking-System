# Class to store and retrieve details of a given transaction
class Transaction:
    """
    A data structure that stores the details of a single transaction 
    (code, amount, source) parsed from the input file.
    """
    
    def __init__(self, code, name, number, amount, misc):
        """
        Initialize a Transaction object with parsed data from transaction file.
        
        Args:
            code (str): Transaction code
            name (str): Account holder name
            number (str): Account number
            amount (str): Transaction amount (will be converted to float)
            misc (str): Miscellaneous field (destination account, biller code, etc.)
        """
        self.code = code
        self.name = name.strip()
        self.number = number
        self.amount = float(amount) if amount else 0.0
        self.misc = misc.strip() if misc else ""
    
    def __str__(self):
        """String representation of the transaction."""
        return f"Transaction[{self.code}, {self.name}, {self.number}, ${self.amount:.2f}, {self.misc}]"
    
    def __repr__(self):
        """Developer representation of the transaction."""
        return f"Transaction('{self.code}', '{self.name}', '{self.number}', {self.amount}, '{self.misc}')"
