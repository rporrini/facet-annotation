#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./scaled-depths.sh types=../evaluation/yago1-type-tree/type-tree.nt destination=../evaluation/labeller-indexes/yago1/depths/depths.csv
