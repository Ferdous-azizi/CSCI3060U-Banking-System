#!/bin/bash
# Phase 3 Automation - Banking Project
# Internal Documentation: Runs tests and avoids confusing expected files with inputs.

javac FrontEnd.java
mkdir -p ./outputs

for dir in ./tests/*/; do
    category=$(basename "$dir")
    echo "Processing Category: $category"

    for input_file in "${dir}"*.txt; do
        # SKIP if the file is an expected output or a terminal log
        [[ "$input_file" == *"_expected"* ]] && continue
        [[ "$input_file" == *"_out"* ]] && continue
        [ -e "$input_file" ] || continue
        
        test_name=$(basename "$input_file" .txt)
        actual_terminal="./outputs/${test_name}.out"
        actual_atf="./outputs/${test_name}.atf"

        # 2. Run the program
        java FrontEnd "current_bank_accounts.txt" "$actual_atf" < "$input_file" > "$actual_terminal"
        
        echo "  [DONE] $test_name"
    done
done