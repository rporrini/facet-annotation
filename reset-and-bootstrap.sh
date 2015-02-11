#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root

signal 'Deleting the knowledge bases'

rm -rf evaluation/dbpedia-* evaluation/yago1-* 

signal 'Deleting the indices'

rm -rf evaluation/labeller-indexes

signal 'Done'

./build-and-test.sh -skip-regression-tests

./experimental-campaign.sh
