#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function clean(){
	./evaluate-results.sh $goldStandard $1 | cut -f3 -d$'\t'
}

function evalAlgorithms(){
	./evaluate-results.sh $goldStandard "$trecResultsDirectory/$1-ml-jaccard--with-context.qrels" > "$trecResultsDirectory/temp/1"
	clean "$trecResultsDirectory/$1-ml-frequency--with-context.qrels" > "$trecResultsDirectory/temp/2"
	clean "$trecResultsDirectory/$1-majority-0.1-with-context.qrels" > "$trecResultsDirectory/temp/3"
	clean "$trecResultsDirectory/$1-majority-0.2-with-context.qrels" > "$trecResultsDirectory/temp/4"
	clean "$trecResultsDirectory/$1-majority-0.3-with-context.qrels" > "$trecResultsDirectory/temp/5"
	clean "$trecResultsDirectory/$1-majority-0.4-with-context.qrels" > "$trecResultsDirectory/temp/6"
	clean "$trecResultsDirectory/$1-majority-0.5-with-context.qrels" > "$trecResultsDirectory/temp/7"
	clean "$trecResultsDirectory/$1-majority-0.6-with-context.qrels" > "$trecResultsDirectory/temp/8"
	clean "$trecResultsDirectory/$1-majority-0.7-with-context.qrels" > "$trecResultsDirectory/temp/9"
	clean "$trecResultsDirectory/$1-majority-0.8-with-context.qrels" > "$trecResultsDirectory/temp/10"
	clean "$trecResultsDirectory/$1-majority-0.9-with-context.qrels" > "$trecResultsDirectory/temp/11"
	clean "$trecResultsDirectory/$1-majority-1.0-with-context.qrels" > "$trecResultsDirectory/temp/12"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

if [ "$1" == "dbpedia" ]
then 
	goldStandard="evaluation/results/gold-standard.qrels"
	trecResultsDirectory="evaluation/results/trec-dbpedia-results"
fi
if [ "$1" == "yago1" ]
then
	goldStandard="evaluation/results/gold-standard-sarawagi.qrels"
	trecResultsDirectory="evaluation/results/trec-yago1-results"
fi

mkdir -p "$trecResultsDirectory/temp/"
evalAlgorithms $1
for file in "$trecResultsDirectory/temp/*"; do paste $file > "$trecResultsDirectory/aggregatedResult"; done; 
rm -rf "$trecResultsDirectory/temp"
