#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function runAlgorithm(){
	tempFile="evaluation/results/$3-$1-results/$1-$2-$5-$4-temp.qrels"
	destination="evaluation/results/$3-$1-results/$1-$2-$5-$4.qrels"
	./run-algorithm.sh $1 $2 $3 $4 $5 > $tempFile
	sed '/\/bin/d' $tempFile |sed '/Running Benchmark/d'|sed '/Building Project/d'|sed '/Done/d'|sed '/Setting Up/d'|sed '/^$/d' > $destination
	rm $tempFile
	signal "$2 $5 done"
}

function runAllAlgorithms(){
	runAlgorithm $1 "ml-frequency" $2 "with-context"
	runAlgorithm $1 "ml-jaccard" $2 "with-context"
	runAlgorithm $1 "ml-ngram" $2 "with-context"
	runAlgorithm $1 "majority" $2 "with-context" "0.1"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

mkdir -p evaluation/results/$2-$1-results
runAllAlgorithms $1 $2

signal "Done"
