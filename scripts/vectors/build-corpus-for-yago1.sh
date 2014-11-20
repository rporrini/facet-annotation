#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
destination=$1
stemming=$2

cd $root
mkdir -p ../../evaluation/labeller-corpora/$destination

time ./corpus.sh source=yago1-properties target=$destination types=yago1/types labels=yago1/labels-english threads=2 stemming=$stemming

cd ../../evaluation/labeller-corpora/$destination
cat * >> yago1-triples
ls * | grep -v yago1-triples | xargs rm -rf

