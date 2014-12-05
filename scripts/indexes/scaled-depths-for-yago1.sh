#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./scaled-depths.sh taxonomy=../evaluation/yago1-type-tree/type-tree.nt types=../evaluation/gold-standards/yago1-types/types.csv destination=../evaluation/labeller-indexes/yago1/depths/depths.csv
