#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.DBPediaWithLabelsQrels ../evaluation/gold-standards/questionnaires/questionnaire-ALL.ods ../evaluation/gold-standards/dbpedia-enhanced-with-labels.qrels
cd ..
./validate.sh ../evaluation/gold-standards/dbpedia-enhanced-with-labels.qrels ../evaluation/gold-standards/dbpedia-enhanced
