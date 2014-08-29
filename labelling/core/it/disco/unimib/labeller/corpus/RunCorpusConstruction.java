package it.disco.unimib.labeller.corpus;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.AcceptAll;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.index.Triples;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.store.NIOFSDirectory;

class RunCorpusConstruction{
	public static void main(String[] args) throws Exception {
		String source = args[0];
		final String target = args[1];
		String typesDirectory = args[2];
		String labelsDirectory = args[3];
		int concurrentThreads = Integer.parseInt(args[4]);
		
		final TripleIndex types = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + typesDirectory)));
		final TripleIndex labels = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + labelsDirectory)));
		
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);
		for(final File file : new File("../evaluation/" + source).listFiles()){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						BulkWriteFile output = new BulkWriteFile(new File(target, file.getName()), 100000);
						new Triples(new InputFile(file)).fill(new TripleCorpus(types, labels, output), new AcceptAll());
						output.flush();
					} catch (Exception e) {
						new Events().error("processing file: " + file, e);
					}
				}
			});
		}
		executor.shutdown();
	    while(!executor.isTerminated()){}
		
		types.closeReader();
		labels.closeReader();
	}
}