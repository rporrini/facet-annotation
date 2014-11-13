#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

dataset=$1
results="evaluation/results"
gs="evaluation/gold-standards"
if [ "$dataset" == "dbpedia-numbers" ]
then
	goldStandard="$gs/dbpedia-enhanced-numbers.qrels"
	trecResultsDirectory="$results/dbpedia-results"
	outputDirectory="$results/dbpedia-numbers-results"
fi
if [ "$dataset" == "dbpedia-without-numbers" ]
then
	goldStandard="$gs/dbpedia-enhanced-without-numbers.qrels"
	trecResultsDirectory="$results/dbpedia-results"
	outputDirectory="$results/dbpedia-without-numbers-results"
fi
if [ "$dataset" == "dbpedia" ]
then 
	goldStandard="$gs/dbpedia-enhanced.qrels"
	trecResultsDirectory="$results/dbpedia-results"
	outputDirectory="$results/dbpedia-results"
fi
if [ "$dataset" == "dbpedia-ontology" ]
then 
	goldStandard="$gs/dbpedia-enhanced-ontology.qrels"
	trecResultsDirectory="$results/dbpedia-ontology-results"
	outputDirectory="$results/dbpedia-ontology-results"
fi
if [ "$dataset" == "dbpedia-with-labels" ]
then
	goldStandard="$gs/dbpedia-enhanced-with-labels.qrels"
	trecResultsDirectory="$results/dbpedia-with-labels-results"
	outputDirectory="$results/dbpedia-with-labels-results"
fi
if [ "$dataset" == "yago1" ]
then
	goldStandard="$gs/yago1-enhanced.qrels"
	trecResultsDirectory="$results/yago1-results"
	outputDirectory="$results/yago1-results"
fi
if [ "$dataset" == "yago1-simple" ]
then
	goldStandard="$gs/yago1-simple.qrels"
	trecResultsDirectory="$results/yago1-simple-results"
	outputDirectory="$results/yago1-simple-results"
fi
if [ "$dataset" == "yago1-no-wrote" ]
then
	goldStandard="$gs/yago1-enhanced-no-wrote.qrels"
	trecResultsDirectory="$results/yago1-results"
	outputDirectory="$results/yago1-no-wrote-results"
fi
if [ "$dataset" == "yago1-wrote" ]
then
	goldStandard="$gs/yago1-enhanced-wrote.qrels"
	trecResultsDirectory="$results/yago1-results"
	outputDirectory="$results/yago1-wrote-results"
fi
if [ "$dataset" == "yago1-simple-no-wrote" ]
then
	goldStandard="$gs/yago1-simple-no-wrote.qrels"
	trecResultsDirectory="$results/yago1-simple-results"
	outputDirectory="$results/yago1-simple-no-wrote-results"
fi
if [ "$dataset" == "yago1-simple-wrote" ]
then
	goldStandard="$gs/yago1-simple-wrote.qrels"
	trecResultsDirectory="$results/yago1-simple-results"
	outputDirectory="$results/yago1-simple-wrote-results"
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
mkdir -p $outputDirectory
for file in "$temp/*"
do 
	paste $file > "$outputDirectory/all-results.csv"
done 
rm -r $temp
