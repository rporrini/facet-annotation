#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
destination=yago1

cd $root
mkdir -p evaluation/labeller-corpora/$destination

./corpus.sh yago1-properties $destination yago1/types yago1/labels-english 2

cd evaluation/labeller-corpora/$destination
cat * >> yago1-triples
ls * | grep -v yago1-triples | xargs rm -rf

