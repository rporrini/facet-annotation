mkdir -p evaluation/gold-standard-sarawagi-enhanced
cd evaluation/
for file in gold-standard-sarawagi/*; do sort --u "$file" | sed 's/&amp;apos;/'\''/g' | sed 's/&amp;amp;/\n/g' | cut -d "(" -f1 | sed 's/&apos;/'\''/g' > "gold-standard-sarawagi-enhanced/${file##*/}";  done;
