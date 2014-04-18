#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
cd $root

./key-value-index.sh dbpedia-types dbpedia/types "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
./key-value-index.sh dbpedia-categories dbpedia/types "http://purl.org/dc/terms/subject"
./key-value-index.sh dbpedia-labels dbpedia/labels "http://www.w3.org/2000/01/rdf-schema#label"
