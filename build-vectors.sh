#!/bin/bash

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
dataset=$1

cd $root
mkdir -p evaluation/labeller-vectors
./word2vec/word2vec -train evaluation/labeller-corpora/$dataset/$dataset-triples -output evaluation/labeller-vectors/$dataset-vectors.txt -cbow 0 -size 600 -window 3 -hs 1 -sample 1e3 -threads 12 -binary 0
