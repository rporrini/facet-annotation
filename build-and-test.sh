#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling

cd $root

signal 'Updating the Repository'
git pull
signal 'Done'

if [[ $1 == yago1 ]] || [[ $1 == dbpedia ]]
then
	kb=$1
fi

scripts/build.sh $kb

cd $project

signal "Running Unit Tests"
java -cp .:'labelling.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.unit.UnitTests
signal "Done"

if [[ $1 != -skip-regression-tests ]] && [[ $2 != -skip-regression-tests ]]
then
	signal "Running Regression Tests"
	java -Xms256m -Xmx3000m -cp .:'labelling.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.regression.RegressionTests
	signal "Done"
fi
