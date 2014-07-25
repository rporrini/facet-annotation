#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

dataset=$1
results="evaluation/results"
if [ "$dataset" == "dbpedia" ]
then 
	goldStandard="$results/gold-standard.qrels"
	trecResultsDirectory="$results/dbpedia-results"
fi
if [ "$dataset" == "dbpedia-with-labels" ]
then
	goldStandard="$results/gold-standard-with-labels.qrels"
	trecResultsDirectory="$results/dbpedia-with-labels-results"
fi
if [ "$dataset" == "yago1" ]
then
	goldStandard="$results/gold-standard-sarawagi.qrels"
	trecResultsDirectory="$results/yago1-results"
fi
temp="$trecResultsDirectory/temp"

mkdir -p $temp
ls $trecResultsDirectory/*.qrels | while read file
do
	fileName=$(basename "$file")
	./evaluate-results.sh $dataset $goldStandard $file | cut -f1 -d$'\t' > "$temp/0000"
	./evaluate-results.sh $dataset $goldStandard $file | cut -f2 -d$'\t' > "$temp/0001"
	./evaluate-results.sh $dataset $goldStandard $file | cut -f3 -d$'\t' > "$temp/$fileName"
done
for file in "$temp/*"
do 
	paste $file > "$trecResultsDirectory/all-results.csv"
done 
rm -r $temp
