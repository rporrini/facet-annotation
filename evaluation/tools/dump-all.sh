#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
d2r=$root/d2rq-0.8.1

cd ..
ontology=7pixel-ontology
property=7pixel-property

mkdir -p $ontology
mkdir -p $property

signal "dump-ontology"
$d2r/./dump-rdf -b http://catalog.shoppydoo.com/ -o $ontology/ontology.nt $root/ontology.ttl

signal "dump-instances"
$d2r/./dump-rdf -b http://catalog.shoppydoo.com/resource/ -o $property/properties.nt $root/properties.ttl

signal "Done"
