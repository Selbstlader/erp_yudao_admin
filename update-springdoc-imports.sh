#!/bin/bash

# Directory where the Java files are located
BASE_DIR="yudao-module-erp"

echo "Starting update of springdoc imports in $BASE_DIR"

# Find all Java files that import org.springdoc.core.GroupedOpenApi
find "$BASE_DIR" -name "*.java" -type f -exec grep -l "import org\.springdoc\.core\.GroupedOpenApi" {} \;

# Function to update springdoc imports in a file
update_in_file() {
  file=$1
  echo "Processing $file..."
  
  # Update the import from org.springdoc.core.GroupedOpenApi to org.springdoc.core.models.GroupedOpenApi
  sed -i '' 's/import org\.springdoc\.core\.GroupedOpenApi/import org.springdoc.core.models.GroupedOpenApi/g' "$file"
}

# Find all Java files and apply the replacements
find "$BASE_DIR" -name "*.java" -type f -exec grep -l "import org\.springdoc\.core\.GroupedOpenApi" {} \; | while read -r file; do
  update_in_file "$file"
done

echo "Update completed!" 