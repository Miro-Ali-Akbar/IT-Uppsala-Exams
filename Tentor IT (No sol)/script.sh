while IFS= read -r directory && IFS= read -r pattern <&3; do     
    find . -maxdepth 1 -type f -name "*${pattern}*.pdf" -exec mv -t "${directory}/" {} \;; 
done < Kurser.txt 3< Kurskoder.txt
