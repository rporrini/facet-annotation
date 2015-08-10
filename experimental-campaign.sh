#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
kb=$1

cd $root

./build-and-test.sh $kb -skip-regression-tests

if [[ $kb == yago1 ]] || [[ $kb == '' ]]
then
	echo "Building indexes for YAGO1"
	scripts/indexes/scaled-depths-for-yago1.sh
	scripts/indexes/build-index-for-yago1.sh

	echo "Running experiments for YAGO1"
	./run-all-algorithms.sh yago1

	echo "Running experiments for YAGO1-SIMPLE"
	./run-all-algorithms.sh yago1-simple
fi

if [[ $kb == dbpedia ]] || [[ $kb == '' ]]
then
	echo "Building indexes for DBPedia"
	scripts/indexes/scaled-depths-for-dbpedia.sh
	scripts/indexes/build-index-for-dbpedia.sh

	echo "Running experiments for DBPedia"
	./run-all-algorithms.sh dbpedia

	echo "Running experiments for DBPedia-ontology"
	./run-all-algorithms.sh dbpedia-ontology

	echo "Running experiments for DBPedia-with-labels"
	./run-all-algorithms.sh dbpedia-with-labels
fi

echo "Evaluating the results"
./evaluate-all-results.sh

