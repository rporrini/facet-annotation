#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
../build.sh > /dev/null

cd ../../labelling
time java -Xms256m -Xmx3000m -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunEvaluation $@

