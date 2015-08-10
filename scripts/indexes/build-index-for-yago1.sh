#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

./key-value-index.sh yago1-types yago1/types "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
./key-value-index.sh yago1-labels-english yago1/labels "http://www.w3.org/2000/01/rdf-schema#label"
./predicate-value-index.sh yago1-properties yago1/properties yago1/types yago1/labels 2 yago1-type-tree/type-tree.nt
