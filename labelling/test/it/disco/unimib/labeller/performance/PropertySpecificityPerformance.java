package it.disco.unimib.labeller.performance;

import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.InputFile;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;
import it.disco.unimib.labeller.properties.PropertyContextSpecificity;
import it.disco.unimib.labeller.properties.PropertyTypesConditionalEntropy;
import it.disco.unimib.labeller.properties.Specificity;

import java.io.File;
import java.util.List;

import org.apache.lucene.store.NIOFSDirectory;
import org.junit.Before;
import org.junit.Test;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
public class PropertySpecificityPerformance extends AbstractBenchmark {

	private IndexFields fields;
	private ContextualizedEvidence evidence;

	@Before
	public void setUp() throws 
	Exception{
		fields = new IndexFields("dbpedia");
		evidence = new ContextualizedEvidence(
							new NIOFSDirectory(new File("../evaluation/labeller-indexes/dbpedia/properties").toPath()), 
							new SimilarityMetricWrapper(new JaccardSimilarity()), 
							fields);
	}
	
	@Test
	public void evaluateTheEntropyOfManyTypes() throws Exception {
		
		Specificity entropy = new PropertyTypesConditionalEntropy(evidence, fields);
		
		evaluateSpecificity(entropy, request());
	}

	@Test
	public void evaluateThePropertyTypeSpecificity() throws Exception {
		
		Specificity entropy = new PropertyContextSpecificity(evidence, fields);
		
		evaluateSpecificity(entropy, request());
	}

	private ContextualizedValues request() {
		return new ContextualizedValues("music albums", new String[]{"http://dbpedia.org/property/artist"});
	}
	
	private void evaluateSpecificity(Specificity specificity, ContextualizedValues request) throws Exception {
		request.setDomains(types());
		specificity.of(request);
	}

	private String[] types() throws Exception {
		List<String> lines = new InputFile(new File("test/it/disco/unimib/labeller/performance/types.txt")).lines();
		return lines.toArray(new String[lines.size()]);
	}
}
