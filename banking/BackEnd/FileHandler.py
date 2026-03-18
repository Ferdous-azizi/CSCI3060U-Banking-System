from Transaction import Transaction
from Account import Account
from read import read_old_bank_accounts

# Utility class to isolate all file parsing and formatting logic
class FileHandler:
    
    # Reads the old master file and converts the raw dictionaries into Account objects
    @staticmethod
    def read_master_file(file_path):
        accounts_dict = {}
        raw_accounts = read_old_bank_accounts(file_path)
        
        for acc in raw_accounts:
            accounts_dict[acc['account_number']] = Account(
                account_number=acc['account_number'],
                name=acc['name'],
                status=acc['status'],
                balance=acc['balance'],
                plan=acc['plan'],
                total_transactions=acc['total_transactions']
            )
        return accounts_dict

    # Parses the merged transaction file line by line to build a list of Transaction objects
    @staticmethod
    def read_transaction_file(file_path):
        transactions = []
        try:
            with open(file_path, 'r') as file:
                for line in file:
                    clean_line = line.rstrip('\n')
                    
                    # Skip empty lines or the End of Session marker
                    if not clean_line or clean_line.startswith("00"):
                        continue
                        
                    # Enforce the strict 41-character limit for transaction records
                    if len(clean_line) != 41:  
                        continue
                        
                    # Slice the string according to the front-end format specifications
                    code = clean_line[0:2] 
                    name = clean_line[3:23]  
                    account_number = clean_line[24:29]  
                    amount = clean_line[30:38]  
                    misc = clean_line[39:41]  

                    transactions.append(Transaction(code, name, account_number, amount, misc))  
                    
        except FileNotFoundError:
            print("Error: file not found")
            
        return transactions

    # Formats the current state of all accounts and writes them to the new master file
    @staticmethod
    def write_new_master_file(file_path, accounts_dict):
        with open(file_path, 'w') as file:
            for acc_num, acc in accounts_dict.items():
                # Skip writing accounts that have been marked for deletion and emptied
                if acc.status == "D" and acc.balance == 0:
                    continue 
                
                # Format each field to ensure the final string is exactly 45 characters
                num_str = acc.account_number.zfill(5)
                name_str = acc.name.ljust(20)[:20]
                status_str = acc.status
                balance_str = f"{acc.balance:08.2f}"
                trans_str = f"{acc.total_transactions:04d}"
                plan_str = acc.plan
                
                file.write(f"{num_str} {name_str} {status_str} {balance_str} {trans_str} {plan_str}\n")
            
            # Append the mandatory EOF marker at the very bottom
            file.write("00000 END_OF_FILE          A 00000.00 0000 NP\n")