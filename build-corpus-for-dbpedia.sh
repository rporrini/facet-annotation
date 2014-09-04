#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
destination=dbpedia

cd $root
mkdir -p evaluation/labeller-corpora/$destination

time ./corpus.sh dbpedia-properties $destination dbpedia/types dbpedia/labels 4
time ./corpus.sh dbpedia-raw-properties $destination dbpedia/types dbpedia/labels 4

cd evaluation/labeller-corpora/$destination
cat * >> dbpedia-triples
ls * | grep -v dbpedia-triples | xargs rm -rf

