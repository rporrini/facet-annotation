#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

./key-value-index.sh yago-types yago/types "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
./key-value-index.sh yago-labels yago/labels "http://www.w3.org/2000/01/rdf-schema#label"
./predicate-value-index.sh yago-properties yago/properties yago/types yago/labels 2
