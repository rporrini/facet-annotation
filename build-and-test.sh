#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
build=$project/build/classes

echo "******* Preparing Infrastructure *******"
if [ ! -d "$project/trank-indexes" ]; then
	cd $project
	wget "http://exascale.info/sites/default/files/uploaded/trank/trank-indexes.tgz"
	tar xvzf trank-indexes.tgz
	rm -f trank-indexes.tgz
	cd ..
fi
echo "******* Done *******"

echo "******* Building Project *******"
cd $project
rm -rf $build
mkdir -p $build
javac -cp .:'lib/*' $(find ./* | grep '\.java') -d $build
cd $build
jar cvfe ../../labelling.jar -C . > /dev/null
chmod 777 ../../labelling.jar
echo "******* Done *******"

echo "******* Running Tests *******"
cd $project
java -cp .:'labelling.jar':'lib/*' org.junit.runner.JUnitCore it.disco.unimib.test.TestSuite
echo "******* Done *******"
