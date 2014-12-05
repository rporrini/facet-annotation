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

cd labelling
signal 'Computing scaled depths'
java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunScaledDepthComputation types=../evaluation/yago1-type-tree/type-tree.nt destination=../evaluation/labeller-indexes/yago1/depths.csv
signal 'Done'
