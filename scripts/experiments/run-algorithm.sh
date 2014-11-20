#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
../build.sh > /dev/null

cd ../../labelling
java -Xms256m -Xmx2000m -cp .:'labelling.jar' it.disco.unimib.labeller.tools.RunEvaluation $@

