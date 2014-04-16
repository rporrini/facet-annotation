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
	sudo apt-get install python
	curl --silent --show-error --retry 5 https://raw.github.com/pypa/pip/master/contrib/get-pip.py | sudo python
	sudo pip install rdflib
fi
command -v bzip2
if [ $? -eq 1 ]; then
	sudo apt-get install bzip2
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
if [ ! -d "geonames-ontologies" ]; then
	mkdir geonames-ontologies
	cd tools
	./download-ontology.py "http://www.geonames.org/ontology/ontology_v3.1.rdf" "../geonames-ontologies/geonames.nt"
	cd ..
fi
if [ ! -d "linkedbrainz-ontologies" ]; then
	mkdir linkedbrainz-ontologies
	cd tools
	./download-ontology.py "http://purl.org/ontology/mo/" "../linkedbrainz-ontologies/mo.nt"
	./download-ontology.py 'http://www.w3.org/2003/01/geo/wgs84_pos' '../linkedbrainz-ontologies/wgs84_pos.nt'
	./download-ontology.py 'http://purl.org/muto/core' '../linkedbrainz-ontologies/muto.nt'
fi
if [ ! -d "dbpedia-types" ]; then
	mkdir dbpedia-types
	cd dbpedia-types
	wget "http://downloads.dbpedia.org/3.8/en/instance_types_en.nt.bz2"
	bunzip2 instance_types_en.nt.bz2
	cd ..
fi
if [ ! -d "dbpedia-properties" ]; then
	mkdir dbpedia-properties
	cd dbpedia-properties
	wget "http://downloads.dbpedia.org/3.8/en/mappingbased_properties_en.nt.bz2"
	bunzip2 mappingbased_properties_en.nt.bz2
	wget "http://downloads.dbpedia.org/3.8/en/specific_mappingbased_properties_en.nt.bz2"
	bunzip2 specific_mappingbased_properties_en.nt.bz2
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

