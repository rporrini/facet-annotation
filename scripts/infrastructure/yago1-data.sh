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
cd $root
signal "Done"

