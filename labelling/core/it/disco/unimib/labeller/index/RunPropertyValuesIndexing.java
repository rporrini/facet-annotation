package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.Events;

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
		
		LuceneBasedIndex types = new KeyValueStore(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + typesDirectory)));
		LuceneBasedIndex labels = new KeyValueStore(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + labelsDirectory)));
		
		final FullTextSearch predicates = new FullTextSearch(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + predicatesDirectory)), 
															types, 
															labels,
															null, 
															new OptionalContext(),
															new KnowledgeBase(knowledgeBase));
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);
		for(final File file : new File("../evaluation/" + source).listFiles()){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						new Triples(new FileSystemConnector(file)).fill(predicates, new AcceptAll());
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
