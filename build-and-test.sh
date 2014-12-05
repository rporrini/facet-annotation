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

scripts/build.sh

cd $project

signal "Running Unit Tests"
java -cp .:'labelling.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.unit.UnitTests
signal "Done"

signal "Running Regression Tests"
java -Xms256m -Xmx3000m -cp .:'labelling.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.regression.RegressionTests
signal "Done"

