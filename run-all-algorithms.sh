#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function runAlgorithm(){
	tempFile="evaluation/results/$3-results/results-$1-$2-$5-temp.txt"
	destination="evaluation/results/$3-results/results-$1-$2-$5.txt"
	./run-algorithm.sh $1 $2 $3 $4 $5 > $tempFile
	sed '/\/bin/d' $tempFile |sed '/Running Benchmark/d'|sed '/Building Project/d'|sed '/Done/d'|sed '/Setting Up/d'|sed '/^$/d' > $destination
	rm $tempFile
	signal "$2 $5 done"
}

function runAllAlgorithms(){
	runAlgorithm $1 "majority" $2 "with-context" "1.0"
	runAlgorithm $1 "majority" $2 "with-context" "0.9"
	runAlgorithm $1 "majority" $2 "with-context" "0.8"
	runAlgorithm $1 "majority" $2 "with-context" "0.7"
	runAlgorithm $1 "majority" $2 "with-context" "0.6"
	runAlgorithm $1 "majority" $2 "with-context" "0.5"
	runAlgorithm $1 "majority" $2 "with-context" "0.4"
	runAlgorithm $1 "majority" $2 "with-context" "0.3"
	runAlgorithm $1 "majority" $2 "with-context" "0.2"
	runAlgorithm $1 "majority" $2 "with-context" "0.1"
	runAlgorithm $1 "ml-frequency" $2 "with-context"
	runAlgorithm $1 "ml-jaccard" $2 "with-context"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

mkdir evaluation/results/$2-results
runAllAlgorithms $1 $2

signal "Done"
