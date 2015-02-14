#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
kb=$1

cd $root

signal 'Deleting all the data and indices'

cd evaluation
if [[ $kb == dbpedia ]]
then
	rm -rf dbpedia-* labeller-indexes/dbpedia labeller-indexes/dbpedia-ontology
	mkdir -p yago1-labels
fi
if [[ $kb == yago1 ]]
then
	rm -rf yago1-* labeller-indexes/yago1
	mkdir -p dbpedia-type-tree dbpedia-category-tree dbpedia-types dbpedia-categories dbpedia-labels dbpedia-properties dbpedia-raw-properties
fi
cd ..

signal 'Done'

./experimental-campaign.sh $kb
