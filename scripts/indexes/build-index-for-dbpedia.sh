#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

./key-value-index.sh dbpedia-types dbpedia/types "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
./key-value-index.sh dbpedia-categories dbpedia/types "http://purl.org/dc/terms/subject"
./key-value-index.sh dbpedia-labels dbpedia/labels "http://www.w3.org/2000/01/rdf-schema#label"
./predicate-value-index.sh dbpedia-properties dbpedia/properties dbpedia/types dbpedia/labels 2 dbpedia-type-tree/type-tree.nt

mkdir -p ../../evaluation/labeller-indexes/dbpedia-ontology
cp -R ../../evaluation/labeller-indexes/dbpedia/properties ../../evaluation/labeller-indexes/dbpedia-ontology

./predicate-value-index.sh dbpedia-raw-properties dbpedia/properties dbpedia/types dbpedia/labels 2 dbpedia-type-tree/type-tree.nt

