#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function run-conditional(){
	if [[ $5 != -skip-baselines ]] 
	then
		run $1 $2 $3 $4
	else
		echo skipped $1 $2 $3 $4 
	fi
}

function run(){
	algorithm=$1
	occurrences=$2
	context=$3
	dataset=$4
	results="evaluation/results/$dataset-results"
	destination="$results/$algorithm-$occurrences-$context.qrels"

	mkdir -p $results
	scripts/experiments/run-algorithm.sh algorithm=$algorithm occurrences=$occurrences context=$context kb=$dataset summary=trec > $destination
	signal "$algorithm $occurrences $context $dataset Done"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

dataset=$1
skip_baselines=$2

run-conditional mh simple partial $dataset $skip_baselines
run-conditional ml simple partial $dataset $skip_baselines
run mhw contextualized partial $dataset
run drc contextualized partial $dataset

