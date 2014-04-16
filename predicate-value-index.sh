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
mkdir -p evaluation/labeller-indexes/$1/predicates

signal "Building Property Index for $2 in $1"
cd $project
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.index.RunPropertyValuesIndexing $1 $2
signal "Done"
