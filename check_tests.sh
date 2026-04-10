#!/bin/bash
# Internal Documentation: Validates results by checking against multiple naming conventions.

OUTPUT_DIR="./outputs"
echo "--- Starting Comparison ---"

for dir in ./tests/*/; do
    # Search for both naming patterns
    for expected_file in "${dir}"*_expected.txt "${dir}"*_out.txt; do
        [ -e "$expected_file" ] || continue
        
        # Determine the base test name
        test_name=$(basename "$expected_file" _expected.txt)
        test_name=$(basename "$test_name" _out.txt)
        
        actual_file="$OUTPUT_DIR/${test_name}.out"

        echo -n "Checking $test_name: "

        if [ ! -f "$actual_file" ]; then
            echo "MISSING"
        elif diff -i -w "$actual_file" "$expected_file" > /dev/null; then
            echo "PASS"
        else
            echo "FAIL"
        fi
    done
done