from Transaction import Transaction
from Account import Account

class FileHandler:
    def read_master_file(file_path):
        """
        Reads and validates the bank account file format with plan type (SP/NP)
        Returns list of accounts and prints fatal errors for invalid format
        """
        # note: starter code logic, provided from assignment instructions, change if needed
        # note: code refrences acc as a dict, change account to dict format or change acc calls in method
        # note: adjusts to be made to account logic as a whole
        # note: code refrences the transaction string, which the current bank account file does not track
        accounts = []
        with open(file_path, 'r') as file:
            for line_num, line in enumerate(file, 1):
                clean_line = line.rstrip('\n')
                
                # Validate line length (now 44 chars to include plan type)
                if len(clean_line) != 45:
                    print(f"ERROR: Fatal error - Line {line_num}: Invalid length ({len(clean_line)} chars, expected 45)")
                    continue

                try:
                    # Extract fields with positional validation
                    account_number = clean_line[0:4]
                    name = clean_line[6:25]  # 20 characters
                    status = clean_line[27]
                    balance_str = clean_line[29:37]  # 8 characters
                    transactions_str = clean_line[38:42]  # 4 characters
                    plan_type = clean_line[43:45]  # 2 characters (SP/NP)

                    # Validate account number
                    if not account_number.isdigit():
                        print(f"ERROR: Fatal error - Line {line_num}: Account number must be 5 digits")
                        continue

                    # Validate status
                    if status not in ('A', 'D'):
                        print(f"ERROR: Fatal error - Line {line_num}: Invalid status '{status}'. Must be 'A' or 'D'")
                        continue

                    # Validate balance format with explicit negative check
                    if balance_str[0] == '-':
                        print(f"ERROR: Fatal error - Line {line_num}: Negative balance detected: {balance_str}")
                        continue
                    
                    if (len(balance_str) != 8 or 
                        balance_str[5] != '.' or 
                        not balance_str[:5].isdigit() or 
                        not balance_str[6:].isdigit()):
                        print(f"ERROR: Fatal error - Line {line_num}: Invalid balance format. Expected XXXXX.XX, got {balance_str}")
                        continue

                    # Validate transaction count
                    if not transactions_str.isdigit():
                        print(f"ERROR: Fatal error - Line {line_num}: Transaction count must be 4 digits")
                        continue

                    # Validate plan type
                    if plan_type not in ('SP', 'NP'):
                        print(f"ERROR: Fatal error - Line {line_num}: Invalid plan type '{plan_type}'. Must be SP or NP")
                        continue

                    # Convert values
                    balance = float(balance_str)
                    transactions = int(transactions_str)

                    # Business rule validation
                    if balance < 0:
                        print(f"ERROR: Fatal error - Line {line_num}: Negative balance detected")
                        continue
                    if transactions < 0:
                        print(f"ERROR: Fatal error - Line {line_num}: Negative transaction not allowed")
                        continue

                    accounts.append({
                        'account_number': account_number.lstrip('0') or '0',
                        'name': name.strip(),
                        'status': status,
                        'balance': balance,
                        'total_transactions': transactions,
                        'plan': plan_type
                    })

                except Exception as e:
                    print(f"ERROR: Fatal error - Line {line_num}: Unexpected error - {str(e)}")
                    continue
        return accounts

    def read_transaction_file(file_path):
        transactions = []
        with open(file_path,'r') as file:
            for line_num, line in enumerate(file, 1):
                clean_line = line.rstrip('\n')
                if len(clean_line) != 40:
                        print(f"ERROR: Fatal error - Line {line_num}: Invalid length ({len(clean_line)} chars, expected 40)")
                        continue
                try:
                    # Extract fields with positional validation
                    code = clean_line[0:2] # 2 characters
                    name = clean_line[3:23]  # 20 characters
                    account_number = clean_line[24:29]  # 8 characters
                    amount = clean_line[30:38]  # 4 characters
                    misc = clean_line[39:41]  # 2 characters

                    #add error handling logic (in testing phase probably)

                    transactions.append(Transaction(code,name,account_number,amount,misc))  
                
                except Exception as e:
                    print(f"ERROR: Fatal error - Line {line_num}: Unexpected error - {str(e)}")
                    continue                
        return transactions

    def write_new_master_file(file_path,accounts):
        #the same as write_current_accounts_file? may not a seperate method for the two
        pass

    def write_current_accounts_file(file_path,accounts):
        """
        Writes Current Bank Accounts File with strict validation
        Format: NNNNN AAAAAAAAAAAAAAAAAAAA S PPPPPPPP TT
        Where TT is account plan (SP or NP)
        """
        # note: starter code logic, provided from assignment instructions, change if needed
        # note: code refrences acc as a dict, change account to dict format or change acc calls in method
        # note: adjusts to be made to account logic as a whole
        # note: code refrences the transaction string, which the current bank account file does not track
        with open(file_path, 'w') as file:
            for acc in accounts:
                # Validate account number
                if not isinstance(acc['account_number'], str) or not acc['account_number'].isdigit():
                    raise ValueError(f"Account number must be numeric string, got {acc['account_number']}")
                if len(acc['account_number']) > 5:
                    raise ValueError(f"Account number exceeds 5 digits: {acc['account_number']}")

                # Validate name
                if len(acc['name']) > 20:
                    raise ValueError(f"Account name exceeds 20 characters: {acc['name']}")

                # Validate status
                if acc['status'] not in ('A', 'D'):
                    raise ValueError(f"Invalid status '{acc['status']}'. Must be 'A' or 'D'")

                # Validate balance with explicit negative check
                if not isinstance(acc['balance'], (int, float)):
                    raise ValueError(f"Balance must be numeric, got {type(acc['balance'])}")
                if acc['balance'] < 0:
                    raise ValueError(f"Negative balance detected: {acc['balance']}")
                if acc['balance'] > 99999.99:
                    raise ValueError(f"Balance exceeds maximum $99999.99: {acc['balance']}")

                # Validate plan type
                plan = acc.get('plan', 'NP')
                if plan not in ('SP', 'NP'):
                    raise ValueError(f"Invalid plan type '{plan}'. Must be SP or NP")

                # Format fields
                acc_num = acc['account_number'].zfill(5)
                name = acc['name'].ljust(20)[:20]
                balance = f"{acc['balance']:08.2f}"

                # Write line (37 chars + plan type = 39 chars total)
                file.write(f"{acc_num} {name} {acc['status']} {balance} {plan}\n")
            
            # Add END_OF_FILE marker
            file.write("00000 END_OF_FILE          A 00000.00 NP\n")