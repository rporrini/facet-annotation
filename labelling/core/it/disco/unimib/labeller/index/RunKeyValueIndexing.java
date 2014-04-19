package it.disco.unimib.labeller.index;

import java.io.File;

import org.apache.lucene.store.SimpleFSDirectory;

public class RunKeyValueIndexing {

	public static void main(String[] args) throws Exception{
		String source = args[0];
		String destination = args[1];
		String predicate = args[2];
		
		AbstractIndex index = new KeyValueStore(new SimpleFSDirectory(new File("../evaluation/labeller-indexes/" + destination)));
		for(File file : new File("../evaluation/" + source).listFiles()){
			System.out.println("processing " + file);
			new Triples(new FileSystemConnector(file)).fill(index, new MatchingPredicate(predicate));
		}
		index.closeWriter();
	}
}
