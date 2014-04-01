#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
build=$project/build/classes

echo "******* Building Project *******"
cd $project
rm -rf $build
mkdir -p $build
javac -cp .:'lib/*' $(find ./* | grep '\.java') -d $build
cd $build
for file in $(find ../../lib/* | grep jar)
do
	jar xf $file
done
rm -r META-INF
jar cvfe ../../labelling.jar -C . > /dev/null
chmod 777 ../../labelling.jar
echo "******* Done *******"

echo "******* Running Tests *******"
cd $project
java -cp .:'labelling.jar' org.junit.runner.JUnitCore it.disco.unimib.test.TestSuite
echo "******* Done *******"
