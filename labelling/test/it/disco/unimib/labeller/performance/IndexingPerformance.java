package it.disco.unimib.labeller.performance;

import org.junit.Test;

import com.carrotsearch.junitbenchmarks.AbstractBenchmark;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;

@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 1, concurrency=2)
public class IndexingPerformance extends AbstractBenchmark{

	@Test
	public void shouldBeExecuted() {
		
	}
}
