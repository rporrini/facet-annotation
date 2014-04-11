#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
project=$root/labelling
build=$project/build/classes

cd $root/evaluation
signal "Setting Up Environment"
command -v python
if [ $? -eq 1 ]; then
	cd utils
	sudo apt-get install python
	curl --silent --show-error --retry 5 https://raw.github.com/pypa/pip/master/contrib/get-pip.py | sudo python
	sudo pip install rdflib
	cd ..
fi
signal "Done"

signal "Setting Up Evaluation Infrastructure"
if [ ! -d "trank-indexes" ]; then
	rm -rf $project/trank-indexes
	wget "http://exascale.info/sites/default/files/uploaded/trank/trank-indexes.tgz"
	tar xvzf trank-indexes.tgz
	rm -f trank-indexes.tgz
	cd ..
fi
if [ ! -d "linkedbrainz" ]; then
	mkdir linkedbrainz
	cd linkedbrainz
	wget -r --no-parent -nH --cut-dirs=4 --reject="*.html" --accept="*nt.gz" http://linkedbrainz.org/rdf/dumps/
	gzip -d *
	cd ..
fi
if [ ! -d "geonames" ]; then
	mkdir geonames
	cd geonames
	wget http://download.geonames.org/all-geonames-rdf.zip
	unzip all-geonames-rdf.zip
	rm -f all-geonames-rdf.zip
	cd ../tools
	./convert.py
	rm all-geonames-rdf.txt
	cd ..
fi
cd $root
signal "Done"

signal "Building Project"
cd $project
rm -rf $build
mkdir -p $build
javac -Xlint:deprecation -cp .:'lib/*' $(find ./* | grep '\.java') -d $build
cd $build
jar cvfe ../../labelling.jar -C . > /dev/null
chmod 777 ../../labelling.jar
signal "Done"

