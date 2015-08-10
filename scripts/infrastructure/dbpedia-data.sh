#!/bin/bash

function signal(){
	echo "******* $1 *******"
}

set -e
relative_path=`dirname $0`
root=`cd $relative_path/../../;pwd`

cd $root/evaluation

signal "Setting Up Evaluation Infrastructure for DBPedia"
if [ ! -d "dbpedia-type-tree" ]; then
	mkdir dbpedia-type-tree
	cd dbpedia-type-tree
	wget "http://downloads.dbpedia.org/3.9/dbpedia_3.9.owl.bz2"
	bunzip2 dbpedia_3.9.owl.bz2
	cd ../tools
	./download-ontology.py "../dbpedia-type-tree/dbpedia_3.9.owl" "../dbpedia-type-tree/dbpedia_3.9.nt"
	cd ../dbpedia-type-tree
	rm dbpedia_3.9.owl
	grep "http://www.w3.org/2000/01/rdf-schema#subClassOf" dbpedia_3.9.nt > type-tree.nt
	rm dbpedia_3.9.nt
	wget -O schema-org.nt "http://www.w3.org/2012/pyRdfa/extract?uri=http://schema.org/docs/schema_org_rdfa.html&format=nt"
	grep "http://www.w3.org/2000/01/rdf-schema#subClassOf" schema-org.nt >> type-tree.nt
	rm schema-org.nt
	cd ..
fi
if [ ! -d "dbpedia-category-tree" ]; then
	mkdir dbpedia-category-tree
	cd dbpedia-category-tree
	wget "http://downloads.dbpedia.org/3.9/en/skos_categories_en.nt.bz2"
	bunzip2 skos_categories_en.nt.bz2
	grep "http://www.w3.org/2004/02/skos/core#broader" skos_categories_en.nt > category-tree.nt
	rm skos_categories_en.nt
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
if [ ! -d "dbpedia-domains" ]; then
	mkdir dbpedia-domains
	cd dbpedia-domains
	wget http://abstat.disco.unimib.it/downloads/dbpedia-3.9-infobox-domains.tar.gz
	tar -zxvf dbpedia-3.9-infobox-domains.tar.gz --strip-components=1
	rm -rf dbpedia-3.9-infobox-domains.tar.gz
	cd ..
fi
if [ ! -d "dbpedia-ranges" ]; then
	mkdir dbpedia-ranges
	cd dbpedia-ranges
	wget http://abstat.disco.unimib.it/downloads/dbpedia-3.9-infobox-ranges.tar.gz
	tar -zxvf dbpedia-3.9-infobox-ranges.tar.gz --strip-components=1
	rm -rf dbpedia-3.9-infobox-ranges.tar.gz
	cd ..
fi

cd $root
signal "Done"

