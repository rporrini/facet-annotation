#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.benchmark.GetSarawagiQrels ../evaluation/gold-standards/yago1-enhanced/ > ../evaluation/results/gold-standard-sarawagi.qrels
cd ..
./validate.sh ../evaluation/results/gold-standard-sarawagi.qrels ../evaluation/gold-standards/yago1-enhanced
