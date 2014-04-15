package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.Events;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.store.NIOFSDirectory;

public class RunPropertyValuesIndexing {

	public static void main(String[] args) throws Exception {
		String dataset = args[0];
		Index types = new KeyValueStore(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + dataset + "/types")));
		Index labels = new KeyValueStore(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + dataset + "/labels")));
		
		final FullTextSearch predicates = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + dataset + "/predicates")), types, labels);
		ExecutorService executor = Executors.newFixedThreadPool(4);
		for(final File file : new File("../evaluation/" + dataset).listFiles()){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						new Triples(new FileSystemConnector(file)).fill(predicates, new LiteralObject());
					} catch (Exception e) {
						new Events().error("processing file: " + file, e);
					}
				}
			});
		}
		executor.shutdown();
	    while(!executor.isTerminated()){}
		
		predicates.closeWriter();
		types.closeReader();
		labels.closeReader();
	}
}
