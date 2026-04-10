#!/bin/bash

# Run the simulation for 7 days [cite: 29]
for day in 1 2 3 4 5 6 7
do
    echo "Processing Day: $day"
    
    # Run the daily cycle [cite: 11]
    sh Daily.sh

    # THE HANDOFF:
    # Copy the new file over the old one, but keep both inside the BackEnd folder 
    cp BackEnd/new_master.txt BackEnd/old_master.txt

    echo "Day $day finished."
done