#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

./build-and-test.sh

echo "Building indexes for YAGO1"
scripts/indexes/build-index-for-yago1.sh

echo "Running experiments for YAGO1"
./run-all-algorithms.sh yago1 && ./evaluate-all-results.sh

echo "Running experiments for YAGO1-SIMPLE"
./run-all-algorithms.sh yago1-simple && ./evaluate-all-results.sh

echo "Building indexes for DBPedia"
scripts/indexes/build-index-for-dbpedia.sh

echo "Running experiments for DBPedia"
./run-all-algorithms.sh dbpedia && ./evaluate-all-results.sh

echo "Running experiments for DBPedia-ontology"
./run-all-algorithms.sh dbpedia-ontology && ./evaluate-all-results.sh

echo "Running experiments for DBPedia-with-labels"
./run-all-algorithms.sh dbpedia-with-labels && ./evaluate-all-results.sh

echo "Performing ttests over the results"
./ttest.sh

