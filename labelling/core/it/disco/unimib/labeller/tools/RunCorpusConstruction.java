package it.disco.unimib.labeller.tools;

import it.disco.unimib.labeller.benchmark.Command;
import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.corpus.BulkWriteFile;
import it.disco.unimib.labeller.corpus.TripleCorpus;
import it.disco.unimib.labeller.index.AcceptAll;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.Triples;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.NIOFSDirectory;

class RunCorpusConstruction{
	public static void main(String[] args) throws Exception {
		Command command = new Command().withArgument("source", "directory under \"evaluation\" folder that contains all the triples for process")
					 .withArgument("target", "directory under \"evaluation/labeller-corpora\" where the resulting corpus is saved")
					 .withArgument("types", "directory under \"evaluation/labeller-indexes\" that contains the types index")
					 .withArgument("labels", "directory under \"evaluation/labeller-indexes\" that contains the labels index")
					 .withArgument("threads", "number of concurrent threads")
					 .withArgument("stemming", "enables or disables stemming, namely yes or no")
					 .parse(args);
		
		String source = command.argumentAsString("source");
		final String target = command.argumentAsString("target");
		String typesDirectory = command.argumentAsString("types");
		String labelsDirectory = command.argumentAsString("labels");
		int concurrentThreads = Integer.parseInt(command.argumentAsString("threads"));
		final String stemming = command.argumentAsString("stemming");
		
		System.out.println("Building corpus for dataset " + source + " in " + target + " using " + typesDirectory + " and " + labelsDirectory + " with stemming=" + stemming);
		
		final EntityValues types = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + typesDirectory).toPath()));
		final EntityValues labels = new EntityValues(new NIOFSDirectory(new File("../evaluation/labeller-indexes/" + labelsDirectory).toPath()));
		
		ExecutorService executor = Executors.newFixedThreadPool(concurrentThreads);
		for(final File file : new File("../evaluation/" + source).listFiles()){
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						BulkWriteFile output = new BulkWriteFile(new File("../evaluation/labeller-corpora/" + target, file.getName()), 50000);
						Analyzer analyzer = analyzer(stemming);
						new Triples(new InputFile(file)).fill(new TripleCorpus(types, labels, output, analyzer), new AcceptAll());
						output.flush();
						analyzer.close();
					} catch (Exception e) {
						Events.verbose().error("processing file: " + file, e);
					}
				}

				private Analyzer analyzer(String analyzerString) {
					if(analyzerString.equals("yes")) return new EnglishAnalyzer();
					return new StandardAnalyzer();
				}
			});
		}
		executor.shutdown();
	    while(!executor.isTerminated()){}
		
		types.closeReader();
		labels.closeReader();
		
		System.out.println("Done");
	}
}