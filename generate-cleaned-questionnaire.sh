#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.benchmark.GetCleanedQuestionnaire ../evaluation/results/majority-05-all-withcontext-enhanced-withlabels-ALL.ods ../evaluation/results/majority-05-all-withcontext-enhanced-withlabels-CLEANED

