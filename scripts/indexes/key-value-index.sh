#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
../build.sh
cd ../../

mkdir -p evaluation/labeller-indexes/$2

signal "Building $3 Index in $2 for $1"
cd labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunKeyValueIndexing $1 $2 $3
signal "Done"
