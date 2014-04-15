package it.disco.unimib.labeller.index;

import java.io.File;

import org.apache.lucene.store.MMapDirectory;

public class RunPropertyValuesIndexing {

	public static void main(String[] args) throws Exception {
		String dataset = "linkedbrainz";
		Index types = new KeyValueStore(new MMapDirectory(new File("../evaluation/labeller-indexes/" + dataset + "/types")));
		Index labels = new KeyValueStore(new MMapDirectory(new File("../evaluation/labeller-indexes/" + dataset + "/labels")));
		
		FullTextSearch predicates = new FullTextSearch(new MMapDirectory(new File("../evaluation/labeller-indexes/" + dataset + "/predicates")), types, labels);
		for(File file : new File("../evaluation/" + dataset).listFiles()){
			new Triples(new FileSystemConnector(file)).fill(predicates, new LiteralObject());
		}
		predicates.close();
	}
}
