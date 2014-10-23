#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.benchmark.SplitQuestionnaire ../evaluation/gold-standards/questionnaires/questionnaire.ods ../evaluation/gold-standards/questionnaires/splitted-questionnaire/questionnaire-part

