#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

sourceDir=$1
targetDir=$2
types=$3
labels=$4
threads=$5
stemming=$6

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
cd $root
./build.sh

signal "Building corpus for dataset $sourceDir in $targetDir using $types and $labels with stemming=$6"
cd $project
java -Xmx4g -cp .:'labelling.jar' it.disco.unimib.labeller.corpus.RunCorpusConstruction source=$sourceDir target=$targetDir types=$types labels=$labels threads=$threads stemming=$stemming
signal "Done"
