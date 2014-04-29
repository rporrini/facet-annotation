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
if [ ! -d "dbpedia-types" ]; then
	mkdir dbpedia-types
	cd dbpedia-types
	wget "http://downloads.dbpedia.org/3.9/en/instance_types_en.nt.bz2"
	bunzip2 instance_types_en.nt.bz2
	wget "http://downloads.dbpedia.org/3.9/en/instance_types_heuristic_en.nt.bz2"
	bunzip2 instance_types_heuristic_en.nt.bz2
	cd ..
fi
if [ ! -d "dbpedia-categories" ]; then
	mkdir dbpedia-categories
	cd dbpedia-categories
	wget "http://downloads.dbpedia.org/3.9/en/article_categories_en.nt.bz2"
	bunzip2 article_categories_en.nt.bz2
	cd ..
fi
if [ ! -d "dbpedia-labels" ]; then
	mkdir dbpedia-labels
	cd dbpedia-labels
	wget "http://downloads.dbpedia.org/3.9/en/labels_en.nt.bz2"
	bunzip2 labels_en.nt.bz2
	wget "http://downloads.dbpedia.org/3.9/en/category_labels_en.nt.bz2"
	bunzip2 category_labels_en.nt.bz2
	wget "http://downloads.dbpedia.org/3.9/dbpedia_3.9.owl.bz2"
	bunzip2 dbpedia_3.9.owl.bz2
	cd ../tools
	./download-ontology.py "../dbpedia-labels/dbpedia_3.9.owl" "../dbpedia-labels/dbpedia_3.9.nt"
	cd ../dbpedia-labels
	rm dbpedia_3.9.owl
	grep "@en" dbpedia_3.9.nt > dbpedia_3.9-en.nt
	rm dbpedia_3.9.nt
	wget -O schema-org.nt "http://www.w3.org/2012/pyRdfa/extract?uri=http://schema.org/docs/schema_org_rdfa.html&format=nt"	
	cd ..
fi
if [ ! -d "dbpedia-properties" ]; then
	mkdir dbpedia-properties
	cd dbpedia-properties
	wget "http://downloads.dbpedia.org/3.9/en/mappingbased_properties_cleaned_en.nt.bz2"
	bunzip2 mappingbased_properties_cleaned_en.nt.bz2
	wget "http://downloads.dbpedia.org/3.9/en/specific_mappingbased_properties_en.nt.bz2"
	bunzip2 specific_mappingbased_properties_en.nt.bz2
	cd ..
fi
if [ ! -d "dbpedia-raw-properties" ]; then
	mkdir dbpedia-raw-properties
	cd dbpedia-raw-properties
	wget http://downloads.dbpedia.org/3.9/en/raw_infobox_properties_en.nt.bz2
	bunzip2 raw_infobox_properties_en.nt.bz2
	split raw_infobox_properties_en.nt -l 1000000
	rm raw_infobox_properties_en.nt
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

