#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
destination=dbpedia

cd $root
mkdir -p evaluation/labeller-corpora/$destination

./corpus.sh dbpedia-properties $destination dbpedia/types dbpedia/labels 6
./corpus.sh dbpedia-raw-properties $destination dbpedia/types dbpedia/labels 6

cd evaluation/labeller-corpora/$destination
cat * >> dbpedia-triples
ls * | grep -v dbpedia-triples | xargs rm -rf

