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
mkdir -p evaluation/labeller-indexes/$2

signal "Building Property Index for dataset $1 in $2 with $3 and $4"
cd $project
java -Xmx4g -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.index.RunPropertyValuesIndexing $1 $2 $3 $4 $5
signal "Done"
