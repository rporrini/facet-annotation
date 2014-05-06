#!/bin/bash
set -e
relative_path=`dirname $0`
root=`cd $relative_path;pwd`

cd $root
./build.sh
cd $root/labelling
rm -rf ../evaluation/gold-standard-sarawagi
java -cp .:'labelling.jar':'lib/*' it.disco.unimib.labeller.benchmark.GetSarawagiGoldStandard ../evaluation/gold-standard-sarawagi/ ../evaluation/tools/annotationData/
cd ..
rm -rf $root/evaluation/gold-standard-sarawagi-enhanced
mkdir -p $root/evaluation/gold-standard-sarawagi-enhanced
cd $root/evaluation
for file in gold-standard-sarawagi/*; do sed -e '/^#/b; s/) /\n/; s/&amp;apos;/'\''/g; s/&amp;amp;/\n/g; s/&apos;/'\''/g; s/&quot; //g; s/&quot;//g; /^-/d; /(.*/d' "$file" | grep -v '^$' | sed 's/#/123456789/g' | sort | uniq | sed 's/123456789/#/g' > "gold-standard-sarawagi-enhanced/${file##*/}"; done;
rm -r ../evaluation/gold-standard-sarawagi

