package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.benchmark.TrecEvalQrels;

import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void onEmptyGoldStandardShouldTrackNoting() throws Exception {
		Summary summary = new TrecEvalQrels("name");
		
		new Benchmark(new AnnotationAlgorithmTestDouble()).on(new GoldStandardFacet[]{}, summary);
		
		assertThat(summary.result(), is(equalTo("")));		
	}
	
	@Test
	public void shouldTrackAnnotatedRankedTypes() throws Exception {
		Summary summary = new TrecEvalQrels("name");
		
		new Benchmark(new AnnotationAlgorithmTestDouble().thatReturns("actual predicate"))
						.on(new GoldStandardFacet[]{
								new GoldStandardFacet(new InputFileTestDouble().withName("amazon_category_expected relation"))
							}, summary);
		
		assertThat(summary.result(), containsString("actual predicate"));
	}
}