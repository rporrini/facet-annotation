#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../../;pwd`

cd $root/evaluation

signal "Setting Up Evaluation Infrastructure for YAGO1"
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
	grep "http://www.w3.org/2000/01/rdf-schema#subClassOf" yago.nt > type-tree.nt
	rm yago.nt
	mkdir ../yago1-type-tree
	mv type-tree.nt ../yago1-type-tree
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
if [ ! -d "yago1-labels-english" ]; then
	mkdir yago1-labels-english
	grep -v @ yago1-labels/yago-labels.nt > yago1-labels-english/yago-labels-english.nt
fi
if [ ! -d "yago1-domains" ]; then
	mkdir yago1-domains
	cd yago1-domains
	wget http://abstat.disco.unimib.it/downloads/yago1-domains.tar.gz
	tar -zxvf yago1-domains.tar.gz --strip-components=1
	rm -rf yago1-domains.tar.gz
	ls | grep yago | while read file; do name=$(echo $file | cut -d'_' -f 4); mv $file $name; done;
	cd ..
fi
if [ ! -d "yago1-ranges" ]; then
	mkdir yago1-ranges
	cd yago1-ranges
	wget http://abstat.disco.unimib.it/downloads/yago1-ranges.tar.gz
	tar -zxvf yago1-ranges.tar.gz --strip-components=1
	rm -rf yago1-ranges.tar.gz
	ls | grep yago | while read file; do name=$(echo $file | cut -d'_' -f 4); mv $file $name; done;
	cd ..
fi
cd $root
signal "Done"

