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
	context=$2
	results=evaluation/results/trec-$dataset-results
	run $results $dataset cml $context
	run $results $dataset ml-frequency $context
	run $results $dataset ml-jaccard $context
	run $results $dataset majority-hit $context
	run $results $dataset majority-hit-jaccard $context
	run $results $dataset majority $context 0.1
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
dataset=$1

runAlgorithm $dataset with-partial-context

signal Done
