#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root/labelling
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.benchmark.ValidateGoldStandard $1 $2
