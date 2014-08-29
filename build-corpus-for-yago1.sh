#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

./corpus.sh yago1-properties yago1 yago1/types yago1/labels-english 2

