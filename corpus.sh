#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

sourceDir=$1
targetDir=$2
types=$3
labels=$4
threads=$5

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
cd $root
./build.sh

mkdir -p evaluation/labeller-corpus/$targetDir

signal "Building corpus for dataset $sourceDir in $targetDir using $types and $labels"
cd $project
java -Xmx4g -cp .:'labelling.jar' it.disco.unimib.labeller.corpus.RunCorpusConstruction $sourceDir $targetDir $types $labels $threads
signal "Done"
