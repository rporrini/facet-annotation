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
if ! command -v python ; then
	sudo apt-get install python
	curl --silent --show-error --retry 5 https://raw.github.com/pypa/pip/master/contrib/get-pip.py | sudo python
	sudo pip install rdflib
fi
if ! command -v bzip2 ; then
	sudo apt-get install bzip2
fi
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
if ! command -v serdi ; then
	cd tools
	wget http://download.drobilla.net/serd-0.18.2.tar.bz2
	tar jxf serd-0.18.2.tar.bz2
	rm serd-0.18.2.tar.bz2
	cd serd-0.18.2
	sudo chmod 777 INSTALL
	set +e	
	./INSTALL
	set -e
	cd ..
fi
if ! command -v 7za ; then
	sudo apt-get install p7zip-full
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
if [ ! -d "yago-types" ]; then
	mkdir yago-types
	cd yago-types
	wget "http://www.mpi-inf.mpg.de/yago-naga/yago/download/yago/yagoTransitiveType.ttl.7z"
	7za e yagoTransitiveType.ttl.7z
	rm yagoTransitiveType.ttl.7z
	grep -v "</text" yagoTransitiveType.ttl | grep -v "</comment" > yagoTransitiveType-cleaned.ttl
	rm yagoTransitiveType.ttl
	serdi -e -b -i turtle -o ntriples yagoTransitiveType-cleaned.ttl > yagoTransitiveType-cleanedout.nt
	rm yagoTransitiveType-cleaned.ttl
	cd ..
fi
if [ ! -d "yago-labels" ]; then
	mkdir yago-labels
	cd yago-labels
	wget "http://www.mpi-inf.mpg.de/yago-naga/yago/download/yago/yagoLabels.ttl.7z"
	7za e yagoLabels.ttl.7z
	rm yagoLabels.ttl.7z
	serdi -e -b -i turtle -o ntriples yagoLabels.ttl | grep "@en" > yagoLabels.nt
	rm yagoLabels.ttl
	cd ..
fi
if [ ! -d "yago-properties" ]; then
	mkdir yago-properties
	cd yago-properties
	wget "http://www.mpi-inf.mpg.de/yago-naga/yago/download/yago/yagoFacts.ttl.7z"
	7za e yagoFacts.ttl.7z
	rm yagoFacts.ttl.7z
	serdi -e -b -i turtle -o ntriples yagoFacts.ttl > yagoFacts.nt
	rm yagoFacts.ttl
	split -l 1000000 yagoFacts.nt yagoFacts-
	rm yagoFacts.nt
	wget "http://www.mpi-inf.mpg.de/yago-naga/yago/download/yago/yagoLiteralFacts.ttl.7z"
	7za e yagoLiteralFacts.ttl.7z
	rm yagoLiteralFacts.ttl.7z
	serdi -e -b -i turtle -o ntriples yagoLiteralFacts.ttl > yagoLiteralFacts.nt
	rm yagoLiteralFacts.ttl
	split -l 1000000 yagoLiteralFacts.nt yagoLiteralFacts-
	rm yagoLiteralFacts.nt
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
jar cvfe ../../labelling.jar -C . > /dev/null
chmod 777 ../../labelling.jar
signal "Done"

