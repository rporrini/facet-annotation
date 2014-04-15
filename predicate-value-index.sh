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
rm -rf evaluation/labeller-indexes/$1/predicates
mkdir -p evaluation/labeller-indexes/$1/predicates

signal "Building Property Index for $1"
cd $project
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.index.RunPropertyValuesIndexing $1
signal "Done"
