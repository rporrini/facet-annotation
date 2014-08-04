#!/bin/bash

function run-trec-eval
{
	trec_eval -m runid -m num_ret -m num_q -m num_rel -m num_rel_ret "$@" | sed 's/_1 /_01 /g;s/_2 /_02 /g;s/_3 /_03 /g;s/_4 /_04 /g;s/_5 /_05 /g;s/_6 /_06 /g;s/_7 /_07 /g;s/_8 /_08 /g;s/_9 /_09 /g'
}

set -e
dataset=$1
gs=$2
run=$3


if [ "$dataset" == "dbpedia" ]
then
	trec_eval_arguments="-M 20 -m map -m iprec_at_recall -m ndcg_cut.01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20"
fi
if [ "$dataset" == "dbpedia-with-labels" ]
then
	trec_eval_arguments="-M 20 -m map -m iprec_at_recall -m ndcg_cut.01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20"
fi
if [ "$dataset" == "yago1" ]
then
	trec_eval_arguments="-M 20 -m recip_rank"
fi

run-trec-eval $trec_eval_arguments $gs $run
run-trec-eval -n -q $trec_eval_arguments $gs $run

