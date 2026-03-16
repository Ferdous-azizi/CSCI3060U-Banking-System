# Class to store and retrieve details of a given transaction
class Transaction:
    """
    Represents a single banking transaction parsed from the merged transaction file.
    Stores all relevant transaction details and provides methods to access and validate
    transaction data.
    """
    
    # Transaction code constants for better readability and maintainability
    TRANSACTION_CODES = {
        'WITHDRAWAL': '01',
        'TRANSFER': '02',
        'PAYBILL': '03',
        'DEPOSIT': '04',
        'CREATE': '05',
        'DELETE': '06',
        'DISABLE': '07',
        'CHANGEPLAN': '08'
    }
    
    # Parameterized constructor for a transaction
    def __init__(self, code, name, number, amount, misc):
        """
        Initialize a Transaction object with parsed data from transaction file.
        
        Args:
            code (str): Transaction code (01-08)
            name (str): Account holder name
            number (str): Account number
            amount (float): Transaction amount
            misc (str): Miscellaneous field (can contain destination account, etc.)
        """
        self.code = code
        self.name = name.strip()  # Remove any extra whitespace
        self.number = number
        self.amount = float(amount) if amount else 0.0  # Convert to float
        self.misc = misc.strip() if misc else ""  # Remove whitespace
    
    def get_transaction_type(self):
        """
        Returns the human-readable transaction type based on the transaction code.
        
        Returns:
            str: Description of transaction type (e.g., 'Withdrawal', 'Deposit')
        """
        transaction_types = {
            '01': 'Withdrawal',
            '02': 'Transfer',
            '03': 'Paybill',
            '04': 'Deposit',
            '05': 'Create Account',
            '06': 'Delete Account',
            '07': 'Disable Account',
            '08': 'Change Plan'
        }
        return transaction_types.get(self.code, 'Unknown')
    
    def is_withdrawal(self):
        """Returns True if transaction is a withdrawal type (01)."""
        return self.code == self.TRANSACTION_CODES['WITHDRAWAL']
    
    def is_transfer(self):
        """Returns True if transaction is a transfer type (02)."""
        return self.code == self.TRANSACTION_CODES['TRANSFER']
    
    def is_paybill(self):
        """Returns True if transaction is a paybill type (03)."""
        return self.code == self.TRANSACTION_CODES['PAYBILL']
    
    def is_deposit(self):
        """Returns True if transaction is a deposit type (04)."""
        return self.code == self.TRANSACTION_CODES['DEPOSIT']
    
    def is_create(self):
        """Returns True if transaction is a create account type (05)."""
        return self.code == self.TRANSACTION_CODES['CREATE']
    
    def is_delete(self):
        """Returns True if transaction is a delete account type (06)."""
        return self.code == self.TRANSACTION_CODES['DELETE']
    
    def is_disable(self):
        """Returns True if transaction is a disable account type (07)."""
        return self.code == self.TRANSACTION_CODES['DISABLE']
    
    def is_changeplan(self):
        """Returns True if transaction is a change plan type (08)."""
        return self.code == self.TRANSACTION_CODES['CHANGEPLAN']
    
    def is_subtractive(self):
        """
        Returns True if transaction subtracts money from an account.
        Includes: withdrawal, transfer, paybill
        """
        subtractive_codes = [
            self.TRANSACTION_CODES['WITHDRAWAL'],
            self.TRANSACTION_CODES['TRANSFER'],
            self.TRANSACTION_CODES['PAYBILL']
        ]
        return self.code in subtractive_codes
    
    def is_additive(self):
        """
        Returns True if transaction adds money to an account.
        Includes: deposit
        """
        return self.code == self.TRANSACTION_CODES['DEPOSIT']
    
    def is_account_modification(self):
        """
        Returns True if transaction modifies account properties without changing balance.
        Includes: create, delete, disable, changeplan
        """
        modification_codes = [
            self.TRANSACTION_CODES['CREATE'],
            self.TRANSACTION_CODES['DELETE'],
            self.TRANSACTION_CODES['DISABLE'],
            self.TRANSACTION_CODES['CHANGEPLAN']
        ]
        return self.code in modification_codes
    
    def get_destination_account(self):
        """
        For transfer transactions, extracts the destination account number from misc field.
        
        Returns:
            str: Destination account number or None if not applicable
        """
        if self.is_transfer() and self.misc:
            # Assuming misc contains destination account number
            return self.misc
        return None
    
    def get_biller_code(self):
        """
        For paybill transactions, extracts the biller code from misc field.
        
        Returns:
            str: Biller code or None if not applicable
        """
        if self.is_paybill() and self.misc:
            return self.misc
        return None
    
    def validate_amount(self):
        """
        Validates that transaction amount is non-negative.
        
        Returns:
            bool: True if amount is valid, False otherwise
        """
        return self.amount >= 0
    
    def to_string(self):
        """
        Returns a formatted string representation of the transaction for logging/debugging.
        
        Returns:
            str: Formatted transaction details
        """
        return (f"Transaction[code={self.code} ({self.get_transaction_type()}), "
                f"name='{self.name}', number={self.number}, "
                f"amount=${self.amount:.2f}, misc='{self.misc}']")
    
    def __str__(self):
        """String representation of the transaction."""
        return self.to_string()
    
    def __repr__(self):
        """Developer representation of the transaction."""
        return f"Transaction('{self.code}', '{self.name}', '{self.number}', {self.amount}, '{self.misc}')"
