package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.AcceptAll;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.Triples;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.store.NIOFSDirectory;

public class RunPropertyValuesIndexing {

	public static void main(String[] args) throws Exception {
		String source = args[0];
		String predicatesDirectory = args[1];
		String typesDirectory = args[2];
		String labelsDirectory = args[3];
		int concurrentThreads = Integer.parseInt(args[4]);
		String knowledgeBase = predicatesDirectory.split("/")[0];
		
		EntityValues types = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + typesDirectory)));
		EntityValues labels = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + labelsDirectory)));
		
		final Evidence predicates = new Evidence(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + predicatesDirectory)), 
															types, 
															labels,
															null, 
															new NoContext(new AllValues()),
															new IndexFields(knowledgeBase));
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);
		for(final File file : new File("../evaluation/" + source).listFiles()){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						new Triples(new InputFile(file)).fill(predicates, new AcceptAll());
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
