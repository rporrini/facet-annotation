#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.DBPediaQrels ../evaluation/results/questionnaire-ALL.ods ../evaluation/gold-standards/dbpedia-enhanced.qrels
cd ..
./validate.sh ../evaluation/gold-standards/dbpedia-enhanced.qrels ../evaluation/gold-standards/dbpedia-enhanced
