package it.disco.unimib.labeller.index;

import java.io.File;

import org.apache.lucene.store.SimpleFSDirectory;

public class RunIndexing {

	public static void main(String[] args) throws Exception{
		String source = args[0];
		String destination = args[1];
		String predicate = args[2];
		
		Index index = new Index(new SimpleFSDirectory(new File("../evaluation/labeller-indexes/" + source + "/" + destination)));
		for(File file : new File("../evaluation/" + source).listFiles()){
			System.out.println("processing " + file);
			new Triples(new FileSystemConnector(file)).fill(index, new MatchingPredicate(predicate));
		}
		index.close();
	}
}
