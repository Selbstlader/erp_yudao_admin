#!/bin/bash

# Directory where the Java files are located
BASE_DIR="yudao-module-erp"

echo "Starting migration of javax to jakarta in $BASE_DIR"

# Function to replace all occurrences of javax to jakarta in a file
replace_in_file() {
  file=$1
  echo "Processing $file..."
  
  # Replace imports: javax.annotation -> jakarta.annotation
  sed -i '' 's/import javax\.annotation\./import jakarta.annotation./g' "$file"
  
  # Replace imports: javax.servlet -> jakarta.servlet
  sed -i '' 's/import javax\.servlet\./import jakarta.servlet./g' "$file"
  
  # Replace imports: javax.validation -> jakarta.validation
  sed -i '' 's/import javax\.validation\./import jakarta.validation./g' "$file"
}

# Find all Java files and apply the replacements
find "$BASE_DIR" -name "*.java" -type f | while read -r file; do
  replace_in_file "$file"
done

echo "Migration completed!" 