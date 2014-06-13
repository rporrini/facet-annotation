#!/bin/bash

function signal(){
	echo ""
	echo "******* $1 *******"
}

function runAlgorithm(){
	signal $2
	./run-algorithm.sh $1 $2 $3 $4 $5
}

function runMajorityAlgorithm(){
	signal "Majority with threshold $5"
	./run-algorithm.sh $1 $2 $3 $4 $5
}

function runAllAlgorithms(){
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "1.0"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.9"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.8"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.7"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.6"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.5"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.4"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.3"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.2"
	runMajorityAlgorithm "yago1" "majority" "qualitative" "with-context" "0.1"
	runAlgorithm "yago1" "ml-frequency" $1 "with-context"
	runAlgorithm "yago1" "ml-jaccard" $1 "with-context"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

runAllAlgorithms $1 > evaluation/results/$1-results-temp.txt

sed '/\/bin/d' evaluation/results/$1-results-temp.txt | sed '/Running Benchmark/d' | sed '/Building Project/d' | sed '/Done/d' | sed '/Setting Up/d' > evaluation/results/$1-results.txt
rm evaluation/results/$1-results-temp.txt

signal "Done"
