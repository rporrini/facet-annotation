#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/../labelling
cd $root

signal 'Updating the Repository'
git pull
signal 'Done'

./build.sh

signal "Running Tests"
cd $project
java -cp .:'labelling.jar' org.junit.runner.JUnitCore it.disco.unimib.labeller.test.TestSuite
signal "Done"
