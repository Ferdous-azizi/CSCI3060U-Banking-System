#!/bin/bash

# 1. Run sessions - Java now looks inside the BackEnd folder for the account list
java FrontEnd BackEnd/old_master.txt session1_log.txt < inputs/session1.txt
java FrontEnd BackEnd/old_master.txt session2_log.txt < inputs/session2.txt

# 2. Merge - We keep the temporary logs in the root to keep it simple
cat session1_log.txt session2_log.txt > merged_transactions.txt

# 3. Run Back End - We tell Python to find 'old_master' and create 'new_master' inside that folder
py BackEnd/BackEndController.py BackEnd/old_master.txt merged_transactions.txt BackEnd/new_master.txt