#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root

rm -rf ../../evaluation/labeller-indexes/yago1/depths

./scaled-depths.sh \
taxonomy=../evaluation/yago1-type-tree/type-tree.nt \
types=../evaluation/gold-standards/yago1-types \
destination=../evaluation/labeller-indexes/yago1/depths

cd ../../evaluation/labeller-indexes/yago1/depths
cat * >> types.csv
ls * | grep -v types.csv | xargs rm -rf

