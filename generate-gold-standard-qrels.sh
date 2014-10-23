#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.benchmark.GetGoldStandardQrels ../evaluation/results/questionnaire-ALL.ods ../evaluation/results/gold-standard.qrels
cd ..
./validate.sh ../evaluation/results/gold-standard.qrels ../evaluation/gold-standards/dbpedia-enhanced
