#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
cd $root
./build.sh

signal "Running Tests"
cd $project
java -cp .:'labelling.jar':'lib/*' org.junit.runner.JUnitCore it.disco.unimib.test.TestSuite
signal "Done"