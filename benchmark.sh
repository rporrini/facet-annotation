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
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.benchmark.labeller.Run
signal "Done"
