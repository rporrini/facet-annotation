#!/bin/bash
function run_ttest()
{
	java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.PerformTTest $@ 
}

function colored_result()
{
	algorithm=$1
	result=$2
	ttest=$3	
	closing='\e[0m'
	opening=$closing	
	if [ $(echo " $ttest < 0.05" | bc) -eq 1 ]
	then
		opening='\e[0;32m'		
	fi
	printf "%-30s %-15s ${opening}%-12s${closing}\n" "$algorithm" "$result" "$ttest"
}

function ttest()
{
	results_directory="../evaluation/results/$1"
	gold_standard="../evaluation/gold-standards/$2.qrels"
	metric=$3
	k=$4
	alg=$5
	
	mh_results=$(run_ttest alg1=$results_directory/$alg-contextualized-partial.qrels alg2=$results_directory/mh-simple-partial.qrels gs=$gold_standard m=$metric k=$k)
	ml_results=$(run_ttest alg1=$results_directory/$alg-contextualized-partial.qrels alg2=$results_directory/ml-simple-partial.qrels gs=$gold_standard m=$metric k=$k)
	
	mh_value=$(echo $mh_results | cut -d' ' -f 2)	
	ml_value=$(echo $ml_results | cut -d' ' -f 2)	
	mhw_value=$(echo $mh_results | cut -d' ' -f 1)	
	mh_ttest=$(echo $mh_results | cut -d' ' -f 3)
	ml_ttest=$(echo $ml_results | cut -d' ' -f 3)
	
	echo "*******" t-testing $metric "for" $alg on $2 "*******"
	printf "%-30s %-15s %-12s\n" "ALGORITHM" "$metric" "P-VALUE"
	colored_result $alg-contextualized-partial $mhw_value 1
	colored_result mh-simple-partial $mh_value $mh_ttest
	colored_result ml-simple-partial $ml_value $ml_ttest
	echo
}

function print_results(){
	ttest dbpedia-results dbpedia-enhanced map 20 $1
	ttest dbpedia-results dbpedia-enhanced-numbers map 20 $1
	ttest dbpedia-results dbpedia-enhanced-without-numbers map 20 $1
	ttest dbpedia-ontology-results dbpedia-enhanced-ontology map 5 $1
	ttest dbpedia-ontology-results dbpedia-enhanced-ontology-numbers map 5 $1
	ttest dbpedia-ontology-results dbpedia-enhanced-ontology-without-numbers map 5 $1
	ttest dbpedia-with-labels-results dbpedia-enhanced-with-labels map 20 $1
	ttest yago1-results yago1-enhanced recip_rank 5 $1
	ttest yago1-simple-results yago1-simple recip_rank 5 $1
	ttest yago1-results yago1-enhanced set_F 1 $1
	ttest yago1-simple-results yago1-simple set_F 1 $1
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
../build.sh > /dev/null
cd ../../labelling

print_results mhw
