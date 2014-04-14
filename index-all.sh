#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
./build-index.sh geonames types "http://www.geonames.org/ontology#featureCode"
./build-index.sh linkedbrainz types "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"

