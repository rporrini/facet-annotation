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
#sudo apt-get install python python-pip bzip2 p7zip-full
#sudo pip install rdflib

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

signal "Setting Up Evaluation Infrastructure"
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
	split mappingbased_properties_cleaned_en.nt -l 1000000 mappingbased-
	rm mappingbased_properties_cleaned_en.nt
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
if [ ! -d "yago1-labels" ]; then
	mkdir yago1
	cd yago1
	wget "http://resources.mpi-inf.mpg.de/yago-naga/yago1_yago2/download/yago1/YAGO1.0.0/n3.zip"
	unzip n3.zip
	rm n3.zip
	wget "http://www.l3s.de/~minack/rdf2rdf/downloads/rdf2rdf-1.0.1-2.3.1.jar"
	java -jar rdf2rdf-1.0.1-2.3.1.jar yago.n3 yago.nt
	rm yago.n3
	rm rdf2rdf-1.0.1-2.3.1.jar
	grep "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" yago.nt > yago-types.nt
	grep "http://www.w3.org/2000/01/rdf-schema#label" yago.nt > yago-labels.nt
	grep -v "http://www.w3.org/1999/02/22-rdf-syntax-ns#type" yago.nt | grep -v "http://www.w3.org/2000/01/rdf-schema#label" > yago-facts.nt
	rm yago.nt
	mkdir ../yago1-properties
	mv yago-facts.nt ../yago1-properties
	mkdir ../yago1-types
	mv yago-types.nt ../yago1-types
	mkdir ../yago1-labels
	mv yago-labels.nt ../yago1-labels
	cd ..
	cd yago1-properties
	split -l 1000000 yago-facts.nt yago-facts-
	rm yago-facts.nt
	cd ..
	rm -r yago1
fi
cd $root
signal "Done"

signal "Building Project"
cd $project
rm -rf $build
mkdir -p $build
javac -Xlint:deprecation -cp .:'lib/*' $(find ./* | grep '\.java') -d $build
cd $build
for file in $(find ../../lib/* | grep .jar)
do
	jar xf $file
done
jar cvfe ../../labelling.jar -C . > /dev/null
chmod 777 ../../labelling.jar
signal "Done"

