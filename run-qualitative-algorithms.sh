#!/bin/bash

function signal(){
	echo "******* threshold $1 *******"
}

function runAlgorithm(){
	signal $5
	./run-algorithm.sh $1 $2 $3 $4 $5
}

function runAllAlgorithms(){
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "1.0"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.9"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.8"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.7"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.6"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.5"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.4"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.3"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.2"
	runAlgorithm "yago1" "majority" "qualitative" "with-context" "0.1"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

runAllAlgorithms > evaluation/results/qualitative-results-temp.txt

sed '/\/bin/d' evaluation/results/qualitative-results-temp.txt | sed '/Running Benchmark/d' | sed '/Building Project/d' | sed '/Done/d' | sed '/Setting Up/d' > evaluation/results/qualitative-results.txt
sed -i '/threshold/i\ ' evaluation/results/qualitative-results.txt
rm evaluation/results/qualitative-results-temp.txt

signal "Done"
