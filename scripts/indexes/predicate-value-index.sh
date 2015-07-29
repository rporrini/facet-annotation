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

signal "Building Property Index for dataset $1 in $2 with $3 and $4"
cd labelling
java -Xmx4g -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunPropertyValuesIndexing $1 $2 $3 $4 $5 $6
signal "Done"
