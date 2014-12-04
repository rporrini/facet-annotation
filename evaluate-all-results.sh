#!/bin/bash

function run-trec-eval
{
	trec_eval -m runid -m num_ret -m num_q -m num_rel -m num_rel_ret "$@" | sed 's/_1 /_01 /g;s/_2 /_02 /g;s/_3 /_03 /g;s/_4 /_04 /g;s/_5 /_05 /g;s/_6 /_06 /g;s/_7 /_07 /g;s/_8 /_08 /g;s/_9 /_09 /g'
}

function evaluate-on-dataset
{	
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
	root='evaluation'
	gs="$root/gold-standards/$1"
	shift
	trec_results="$root/results/$1"
	shift
	output="$root/results/$1"
	shift
	trec_eval="$@"
	
	temp="$trec_results/temp"
	mkdir -p $temp
	ls $trec_results/*.qrels | while read file
	do
		fileName=$(basename "$file")
		evaluate-on-dataset $gs $file $trec_eval | cut -f1 -d$'\t' > "$temp/0000"
		evaluate-on-dataset $gs $file $trec_eval | cut -f2 -d$'\t' > "$temp/0001"
		evaluate-on-dataset $gs $file $trec_eval | cut -f3 -d$'\t' > "$temp/$fileName"
	done
	mkdir -p "$output"
	for file in "$temp/*"
	do 
		paste $file > "$output/all-results.csv"
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

evaluate dbpedia-enhanced-with-labels.qrels dbpedia-with-labels-results dbpedia-with-labels-results $trec_eval_dbpedia
evaluate dbpedia-enhanced.qrels dbpedia-results dbpedia-results $trec_eval_dbpedia
evaluate dbpedia-enhanced-numbers.qrels dbpedia-results dbpedia-numbers-results $trec_eval_dbpedia
evaluate dbpedia-enhanced-without-numbers.qrels dbpedia-results dbpedia-without-numbers-results $trec_eval_dbpedia
evaluate dbpedia-enhanced-ontology.qrels dbpedia-ontology-results dbpedia-ontology-results $trec_eval_dbpedia_ontology
evaluate dbpedia-enhanced-ontology-numbers.qrels dbpedia-ontology-results dbpedia-ontology-numbers-results $trec_eval_dbpedia_ontology
evaluate dbpedia-enhanced-ontology-without-numbers.qrels dbpedia-ontology-results dbpedia-ontology-without-numbers-results $trec_eval_dbpedia_ontology
evaluate yago1-enhanced.qrels yago1-results yago1-results $trec_eval_yago1
evaluate yago1-enhanced-no-wrote.qrels yago1-results yago1-no-wrote-results $trec_eval_yago1
evaluate yago1-enhanced-wrote.qrels yago1-results yago1-wrote-results $trec_eval_yago1
evaluate yago1-simple.qrels yago1-simple-results yago1-simple-results $trec_eval_yago1
evaluate yago1-simple-no-wrote.qrels yago1-simple-results yago1-simple-no-wrote-results $trec_eval_yago1
evaluate yago1-simple-wrote.qrels yago1-simple-results yago1-simple-wrote-results $trec_eval_yago1

scripts/experiments/ttest.sh


