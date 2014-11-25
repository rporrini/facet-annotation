package it.disco.unimib.labeller.performance;

import it.disco.unimib.labeller.regression.CommandLineBenchmarkSimulation;

import org.junit.BeforeClass;
import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency=1)
public class FacetAnnotationPerformance extends AbstractBenchmark {

	@BeforeClass
	public static void annotateAnotherFacetInOrderToEnsureThatLuceneDoesNotCacheResults() throws Exception{
		
		new CommandLineBenchmarkSimulation().run("dbpedia", 1888395491);
	}
	
	@Test
	public void runAnnotationOnNumericValuesForDBPedia() throws Exception {
		
		new CommandLineBenchmarkSimulation().run("dbpedia", 2043956000);
	}
}
