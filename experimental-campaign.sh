#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

./build-and-test.sh

echo "Building indexes for YAGO1"
scripts/indexes/build-index-for-yago1.sh
scripts/indexes/scaled-depths-for-yago1.sh

echo "Running experiments for YAGO1"
./run-all-algorithms.sh yago1

echo "Running experiments for YAGO1-SIMPLE"
./run-all-algorithms.sh yago1-simple

echo "Building indexes for DBPedia"
scripts/indexes/build-index-for-dbpedia.sh
scripts/indexes/scaled-depths-for-dbpedia.sh

echo "Running experiments for DBPedia"
./run-all-algorithms.sh dbpedia

echo "Running experiments for DBPedia-ontology"
./run-all-algorithms.sh dbpedia-ontology

echo "Running experiments for DBPedia-with-labels"
./run-all-algorithms.sh dbpedia-with-labels

echo "Evaluating the results"
./evaluate-all-results.sh

