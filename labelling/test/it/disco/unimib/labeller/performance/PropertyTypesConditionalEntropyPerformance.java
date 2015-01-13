package it.disco.unimib.labeller.performance;

import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.properties.PropertyTypesConditionalEntropy;

import java.io.File;
import java.util.List;

import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Test;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
public class PropertyTypesConditionalEntropyPerformance extends AbstractBenchmark {

	@Test
	public void evaluateTheEntropyOfManyTypes() throws Exception {
		
		NIOFSDirectory directory = new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties"));
		IndexFields fields = new IndexFields("dbpedia");
		SimilarityMetricWrapper score = new SimilarityMetricWrapper(new JaccardSimilarity());
		
		PropertyTypesConditionalEntropy entropy = new PropertyTypesConditionalEntropy(new ContextualizedEvidence(directory, score, fields), fields);
		
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"http://dbpedia.org/property/artist"});
		request.setDomainTypes(types());
		entropy.of(request);
	}

	private String[] types() throws Exception {
		List<String> lines = new InputFile(new File("test/it/disco/unimib/labeller/performance/types.txt")).lines();
		return lines.toArray(new String[lines.size()]);
	}
}
