#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
../build.sh

cd ../../labelling
java -cp .:'labelling.jar' it.disco.unimib.labeller.tools.BuildYAGO1GoldStandard ../evaluation/gold-standards/gold-standard-yago1/ ../evaluation/tools/annotationData/
cd ../evaluation/gold-standards

rm -rf yago1-enhanced
mkdir -p yago1-enhanced

for file in gold-standard-yago1/*; do sed -e '/^#/b; s/) /\n/; s/&amp;apos;/'\''/g; s/&amp;amp;/\n/g; s/&apos;/'\''/g; s/&quot; //g; s/&quot;//g; /^-/d; s/(.*//g' "$file" | grep -v '^$' | sed 's/#/123456789/g' | sort | uniq | sed 's/123456789/#/g' > "yago1-enhanced/${file##*/}"; done;

rm -r gold-standard-yago1

