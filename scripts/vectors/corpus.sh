#!/bin/bash

sourceDir=$1
targetDir=$2
types=$3
labels=$4
threads=$5
stemming=$6

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root/..
./build.sh

cd ../labelling
java -Xmx4g -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunCorpusConstruction $@
