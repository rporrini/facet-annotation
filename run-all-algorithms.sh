#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function run(){
	results=$1
	dataset=$2
	algorithm=$3
	context=$4
	threshold=$5
	destination="$results/$dataset-$algorithm-$threshold-$context.qrels"
	tempFile="$destination.temp"
	mkdir -p $results
	./run-algorithm.sh $dataset $algorithm trec $context $threshold > $tempFile
	sed '/\/bin/d' $tempFile |sed '/Running Benchmark/d'|sed '/Building Project/d'|sed '/Done/d'|sed '/Setting Up/d'|sed '/^$/d' > $destination
	rm $tempFile
	signal "$algorithm $threshold $context Done"
}

function runAlgorithm(){
	dataset=$1
	algorithm=$2
	threshold=$3
	results=evaluation/results/trec-$dataset-results
	run $results $dataset $algorithm with-context $threshold
	run $results $dataset $algorithm without-context $threshold
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
dataset=$1

runAlgorithm $dataset ml-frequency
runAlgorithm $dataset ml-jaccard
runAlgorithm $dataset ml-ngram
runAlgorithm $dataset majority-hit
runAlgorithm $dataset majority-hit-jaccard
runAlgorithm $dataset majority 0.1

signal Done
