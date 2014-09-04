package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.benchmark.TrecEval;

import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void onEmptyGoldStandardShouldTrackNoting() throws Exception {
		Summary summary = new TrecEval("name");
		
		new Benchmark(new AnnotationAlgorithmTestDouble()).on(new GoldStandardGroup[]{}, summary);
		
		assertThat(summary.result(), is(equalTo("")));		
	}
	
	@Test
	public void shouldTrackAnnotatedRankedTypes() throws Exception {
		Summary summary = new TrecEval("name");
		
		new Benchmark(new AnnotationAlgorithmTestDouble().thatReturns("actual predicate"))
						.on(new GoldStandardGroup[]{
								new GoldStandardGroup(new InputFileTestDouble().withName("amazon_category_expected relation"))
							}, summary);
		
		assertThat(summary.result(), containsString("actual predicate"));
	}
}