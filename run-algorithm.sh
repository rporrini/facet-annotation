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

signal "Running Benchmark"
cd $project
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.benchmark.Run $1 $2 $3 $4 $5
signal "Done"
