#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

function run(){
	algorithm=$1
	occurrences=$2
	context=$3
	dataset=$4
	results="evaluation/results/$dataset-results"
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

#run mh simple no $dataset
#run mh simple partial $dataset
#run mh simple "complete" $dataset
#run mh contextualized no $dataset
#run mh contextualized partial $dataset
#run mh contextualized "complete" $dataset
#run mhw simple partial $dataset
#run mhw simple "complete" $dataset
#run mhw contextualized partial $dataset
#run mhw contextualized "complete" $dataset
#run mhw simple no $dataset
#run mhw contextualized no $dataset
#run mhwv simple partial $dataset
#run mhwv simple "complete" $dataset
#run mhwv contextualized partial $dataset
#run mhwv contextualized "complete" $dataset
#run mhwv simple no $dataset
#run mhwv contextualized no $dataset
#run mhwcv simple partial $dataset
#run mhwcv simple "complete" $dataset
#run mhwcv contextualized partial $dataset
#run mhwcv contextualized "complete" $dataset
run mhwcv simple no $dataset
run mhwcv contextualized no $dataset
#run ml simple no $dataset
#run ml simple partial $dataset
#run ml simple "complete" $dataset
#run ml contextualized no $dataset
#run ml contextualized partial $dataset
#run ml contextualized "complete" $dataset
