#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
destination=$1
stemming=$2

cd $root
mkdir -p ../../evaluation/labeller-corpora/$destination

time ./corpus.sh source=dbpedia-properties target=$destination types=dbpedia/types labels=dbpedia/labels threads=4 stemming=$stemming
time ./corpus.sh source=dbpedia-raw-properties target=$destination types=dbpedia/types labels=dbpedia/labels threads=4 stemming=$stemming

cd ../../evaluation/labeller-corpora/$destination
cat * >> dbpedia-triples
ls * | grep -v dbpedia-triples | xargs rm -rf

