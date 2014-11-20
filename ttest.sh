#!/bin/bash
function run_ttest()
{
	java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.PerformTTest $@ 
}

function colored_result()
{
	result=$1
	algorithm=$2
	closing='\e[0m'
	opening=$closing	
	if [ $(echo " $result < 0.05" | bc) -eq 1 ]
	then
		opening='\e[0;32m'		
	fi	
	echo -e "$algorithm	${opening}$result${closing}"
}

function ttest()
{
	results_directory="../evaluation/results/$1"
	gold_standard="../evaluation/gold-standards/$2.qrels"
	metric=$3
	k=$4
	
	mh_ttest=$(run_ttest alg1=$results_directory/mhw-contextualized-partial.qrels alg2=$results_directory/mh-simple-partial.qrels gs=$gold_standard m=$metric k=$k | tail -1)
	ml_ttest=$(run_ttest alg1=$results_directory/mhw-contextualized-partial.qrels alg2=$results_directory/ml-simple-partial.qrels gs=$gold_standard m=$metric k=$k | tail -1)
	
	echo "*******" t-testing "for" $metric mhw-contextualized-partial on $2 "*******"
	colored_result $mh_ttest mh-simple-partial
	colored_result $ml_ttest ml-simple-partial
	echo
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
scripts/build.sh > /dev/null
cd labelling

ttest dbpedia-results dbpedia-enhanced map 20
ttest dbpedia-results dbpedia-enhanced-numbers map 20
ttest dbpedia-results dbpedia-enhanced-without-numbers map 20
ttest dbpedia-ontology-results dbpedia-enhanced-ontology map 5
ttest dbpedia-ontology-results dbpedia-enhanced-ontology-numbers map 5
ttest dbpedia-ontology-results dbpedia-enhanced-ontology-without-numbers map 5
ttest dbpedia-with-labels-results dbpedia-enhanced-with-labels map 20
ttest yago1-results yago1-enhanced recip_rank 5
ttest yago1-simple-results yago1-simple recip_rank 5
ttest yago1-results yago1-enhanced set_F 1
ttest yago1-simple-results yago1-simple set_F 1
