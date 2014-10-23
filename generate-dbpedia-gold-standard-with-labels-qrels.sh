#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.benchmark.GetGoldStandardLabelsQrels ../evaluation/results/questionnaire-ALL.ods ../evaluation/results/gold-standard-with-labels.qrels
cd ..
./validate.sh ../evaluation/results/gold-standard-with-labels.qrels ../evaluation/gold-standards/dbpedia-enhanced
