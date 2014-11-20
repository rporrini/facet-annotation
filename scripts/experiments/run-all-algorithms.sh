#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function run(){
	algorithm=$1
	occurrences=$2
	context=$3
	dataset=$4
	results="../../evaluation/results/$dataset-results"
	destination="$results/$algorithm-$occurrences-$context.qrels"

	mkdir -p $results
	./run-algorithm.sh algorithm=$algorithm occurrences=$occurrences context=$context kb=$dataset summary=trec > $destination
	signal "$algorithm $occurrences $context $dataset Done"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

dataset=$1

run mh simple partial $dataset
run ml simple partial $dataset
run mhw contextualized partial $dataset

run mh simple no $dataset
run ml simple no $dataset

