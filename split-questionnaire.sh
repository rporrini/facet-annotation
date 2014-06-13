#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.benchmark.SplitQuestionnaire ../evaluation/results/questionnaire.ods ../evaluation/results/splitted-questionnaire/questionnaire-part

