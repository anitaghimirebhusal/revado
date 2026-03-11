#!/bin/bash

API_URL="http://localhost:8082/books"

echo "Starting book data seeding to $API_URL..."

books=(
  "The Great Gatsby|F. Scott Fitzgerald|Fiction|180|1925"
  "1984|George Orwell|Dystopian|328|1949"
  "Animal Farm|George Orwell|Dystopian|112|1945"
  "The Hobbit|J.R.R. Tolkien|Fantasy|310|1937"
  "Brave New World|Aldous Huxley|Dystopian|268|1932"
  "Moby Dick|Herman Melville|Classic|635|1851"
  "Sapiens|Yuval Noah Harari|Science|443|2011"
  "The Catcher in the Rye|J.D. Salinger|Fiction|234|1951"
)

for book_info in "${books[@]}"; do
  IFS="|" read -r title author genre pages year <<< "$book_info"

  echo "Posting: $title by $author..."

  curl --location "$API_URL" \
    --header 'Content-Type: application/json' \
    --data "{
        \"title\": \"$title\",
        \"author\": \"$author\",
        \"genre\": \"$genre\",
        \"pages\": $pages,
        \"publishedYear\": $year
    }"

  echo -e "\n Done."
done

echo "Seeding complete!"
