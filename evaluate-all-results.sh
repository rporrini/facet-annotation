#!/bin/bash

function run-trec-eval
{
	trec_eval -m runid -m num_ret -m num_q -m num_rel -m num_rel_ret "$@" | sed 's/_1 /_01 /g;s/_2 /_02 /g;s/_3 /_03 /g;s/_4 /_04 /g;s/_5 /_05 /g;s/_6 /_06 /g;s/_7 /_07 /g;s/_8 /_08 /g;s/_9 /_09 /g'
}

function evaluate-on-dataset
{	
	dataset=$1
	shift	
	gs=$1
	shift
	run=$1
	shift
	arguments="$@"

	run-trec-eval $arguments $gs $run
	run-trec-eval -n -q $arguments $gs $run
}

function evaluate
{
	dataset=$1
	shift
	trec_eval="$@"
	results="evaluation/results"
	gs="evaluation/gold-standards"

	if [ "$dataset" == "dbpedia" ]
	then 
		goldStandard="$gs/dbpedia-enhanced.qrels"
		trecResultsDirectory="$results/dbpedia-results"
		outputDirectory="$results/dbpedia-results"
	fi
	if [ "$dataset" == "dbpedia-numbers" ]
	then
		goldStandard="$gs/dbpedia-enhanced-numbers.qrels"
		trecResultsDirectory="$results/dbpedia-results"
		outputDirectory="$results/dbpedia-numbers-results"
	fi
	if [ "$dataset" == "dbpedia-without-numbers" ]
	then
		goldStandard="$gs/dbpedia-enhanced-without-numbers.qrels"
		trecResultsDirectory="$results/dbpedia-results"
		outputDirectory="$results/dbpedia-without-numbers-results"
	fi
	if [ "$dataset" == "dbpedia-ontology" ]
	then 
		goldStandard="$gs/dbpedia-enhanced-ontology.qrels"
		trecResultsDirectory="$results/dbpedia-ontology-results"
		outputDirectory="$results/dbpedia-ontology-results"
	fi
	if [ "$dataset" == "dbpedia-ontology-numbers" ]
	then 
		goldStandard="$gs/dbpedia-enhanced-ontology-numbers.qrels"
		trecResultsDirectory="$results/dbpedia-ontology-results"
		outputDirectory="$results/dbpedia-ontology-numbers-results"
	fi
	if [ "$dataset" == "dbpedia-ontology-without-numbers" ]
	then 
		goldStandard="$gs/dbpedia-enhanced-ontology-without-numbers.qrels"
		trecResultsDirectory="$results/dbpedia-ontology-results"
		outputDirectory="$results/dbpedia-ontology-without-numbers-results"
	fi
	if [ "$dataset" == "dbpedia-with-labels" ]
	then
		goldStandard="$gs/dbpedia-enhanced-with-labels.qrels"
		trecResultsDirectory="$results/dbpedia-with-labels-results"
		outputDirectory="$results/dbpedia-with-labels-results"
	fi
	if [ "$dataset" == "yago1" ]
	then
		goldStandard="$gs/yago1-enhanced.qrels"
		trecResultsDirectory="$results/yago1-results"
		outputDirectory="$results/yago1-results"
	fi
	if [ "$dataset" == "yago1-simple" ]
	then
		goldStandard="$gs/yago1-simple.qrels"
		trecResultsDirectory="$results/yago1-simple-results"
		outputDirectory="$results/yago1-simple-results"
	fi
	if [ "$dataset" == "yago1-no-wrote" ]
	then
		goldStandard="$gs/yago1-enhanced-no-wrote.qrels"
		trecResultsDirectory="$results/yago1-results"
		outputDirectory="$results/yago1-no-wrote-results"
	fi
	if [ "$dataset" == "yago1-wrote" ]
	then
		goldStandard="$gs/yago1-enhanced-wrote.qrels"
		trecResultsDirectory="$results/yago1-results"
		outputDirectory="$results/yago1-wrote-results"
	fi
	if [ "$dataset" == "yago1-simple-no-wrote" ]
	then
		goldStandard="$gs/yago1-simple-no-wrote.qrels"
		trecResultsDirectory="$results/yago1-simple-results"
		outputDirectory="$results/yago1-simple-no-wrote-results"
	fi
	if [ "$dataset" == "yago1-simple-wrote" ]
	then
		goldStandard="$gs/yago1-simple-wrote.qrels"
		trecResultsDirectory="$results/yago1-simple-results"
		outputDirectory="$results/yago1-simple-wrote-results"
	fi

	temp="$trecResultsDirectory/temp"
	mkdir -p $temp
	ls $trecResultsDirectory/*.qrels | while read file
	do
		fileName=$(basename "$file")
		evaluate-on-dataset $dataset $goldStandard $file $trec_eval | cut -f1 -d$'\t' > "$temp/0000"
		evaluate-on-dataset $dataset $goldStandard $file $trec_eval | cut -f2 -d$'\t' > "$temp/0001"
		evaluate-on-dataset $dataset $goldStandard $file $trec_eval | cut -f3 -d$'\t' > "$temp/$fileName"
	done
	mkdir -p $outputDirectory
	for file in "$temp/*"
	do 
		paste $file > "$outputDirectory/all-results.csv"
	done 
	rm -r $temp
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

trec_eval_dbpedia="-M 20 -N 20 -m set_P -m set_recall -m set_F -m map -m ndcg_cut.01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20"
trec_eval_dbpedia_ontology="-M 5 -N 5 -m set_P -m set_recall -m set_F -m map -m ndcg_cut.01,02,03,04,05"
trec_eval_yago1="-M 5 -N 5 -m recip_rank -m set_P -m set_recall -m set_F"

evaluate dbpedia-with-labels $trec_eval_dbpedia
evaluate dbpedia $trec_eval_dbpedia
evaluate dbpedia-numbers $trec_eval_dbpedia
evaluate dbpedia-without-numbers $trec_eval_dbpedia
evaluate dbpedia-ontology $trec_eval_dbpedia_ontology
evaluate dbpedia-ontology-numbers $trec_eval_dbpedia_ontology
evaluate dbpedia-ontology-without-numbers $trec_eval_dbpedia_ontology
evaluate yago1 $trec_eval_yago1
evaluate yago1-no-wrote $trec_eval_yago1
evaluate yago1-wrote $trec_eval_yago1
evaluate yago1-simple $trec_eval_yago1
evaluate yago1-simple-no-wrote $trec_eval_yago1
evaluate yago1-simple-wrote $trec_eval_yago1


