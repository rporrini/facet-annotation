#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../;pwd`
project=$root/labelling
build=$project/build/classes
kb=$1

cd $root/evaluation

signal "Setting Up Environment"
sudo apt-get install python python-pip bzip2 p7zip-full
sudo pip install rdflib

if ! command -v trec_eval ; then
	cd tools
	wget http://trec.nist.gov/trec_eval/trec_eval_latest.tar.gz
	tar xvzf trec_eval_latest.tar.gz
	cd trec_eval.9.0
	make
	sudo make install
	make quicktest
	cd ..
	rm trec_eval_latest.tar.gz	
	cd ..
fi
signal "Done"

cd $root

if [[ $kb == dbpedia ]] || [[ $kb == '' ]]
then
	scripts/infrastructure/dbpedia-data.sh
fi
if [[ $kb == yago1 ]] || [[ $kb == '' ]]
then
	scripts/infrastructure/yago1-data.sh
fi

signal "Building Project"
cd $project
rm -rf $build
mkdir -p $build
javac -encoding utf8 -cp .:'lib/*' $(find ./* | grep '\.java') -d $build
cd $build
for file in $(find ../../lib/* | grep .jar)
do
	jar xf $file
done
jar cvfe ../../labelling.jar -C . > /dev/null
chmod 777 ../../labelling.jar
signal "Done"

