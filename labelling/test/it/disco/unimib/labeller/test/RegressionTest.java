package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.BenchmarkParameters;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.SingleFacet;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.tools.RunEvaluation;
import it.disco.unimib.labeller.tools.TrecResultPredicate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class RegressionTest {

	@Test
	public void dbpediaExperiments() throws Exception {
		int movieDirectors = 1888395491;
		String knowledgeBase = "dbpedia";
		
		List<TrecResultPredicate> results = runBenchmark(knowledgeBase, movieDirectors);
		
		checkNonRegression(results, 
						   27,
						   "http://dbpedia.org/property/director",
						   "http://dbpedia.org/ontology/director",
						   "http://dbpedia.org/ontology/writer",
						   "http://dbpedia.org/property/writer");
	}
	
	private void checkNonRegression(List<TrecResultPredicate> results, int expectedSize, String... predicates){
		assertThat(results, hasSize(expectedSize));
		for(int i=0; i<predicates.length; i++){
			assertThat(results.get(i).value(), equalTo(predicates[i]));
		}
	}

	private List<TrecResultPredicate> runBenchmark(String knowledgeBase, int movieDirectors) throws Exception {
		BenchmarkParameters parameters = RunEvaluation.benchmarkParameters(new String[]{
				"kb=" + knowledgeBase,
				"algorithm=mhw",
				"occurrences=contextualized",
				"context=partial",
				"summary=trec"
		});
		Summary summary = parameters.analysis();
		GoldStandard goldStandard = new SingleFacet(parameters.goldStandard(), movieDirectors);
		RunEvaluation.runBenchmark(summary, parameters.algorithm(), goldStandard);
		List<TrecResultPredicate> results = new ArrayList<TrecResultPredicate>();
		for(String line : summary.result().split("\n")){
			results.add(new TrecResultPredicate(line));
		}
		return results;
	}
}
