#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
cd $root
./build.sh > /dev/null

cd $project
java -Xms256m -Xmx2000m -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunEvaluation $@

