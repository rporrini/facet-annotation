#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
cd $root
./build.sh
rm -rf evaluation/labeller-indexes/$2
mkdir -p evaluation/labeller-indexes/$2

signal "Building $3 Index in $2 for $1"
cd $project
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.index.RunKeyValueIndexing $1 $2 $3
signal "Done"
