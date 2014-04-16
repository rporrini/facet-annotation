#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`
cd $root
./key-value-index.sh geonames all/types "http://www.geonames.org/ontology#featureCode"
./key-value-index.sh geonames-ontologies all/labels 'http://www.w3.org/2004/02/skos/core#prefLabel'
./predicate-value-index.sh all geonames
./key-value-index.sh linkedbrainz all/types "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
./key-value-index.sh linkedbrainz-ontologies all/labels "http://www.w3.org/2000/01/rdf-schema#label"
./predicate-value-index.sh all linkedbrainz

