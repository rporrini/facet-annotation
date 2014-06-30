package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Qualitative;


import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void onEmptyGoldStandardShouldTrackNoting() throws Exception {
		Qualitative summary = new Qualitative();
		
		new Benchmark(new AnnotationAlgorithmTestDouble()).on(new GoldStandardGroup[]{}, summary);
		
		assertThat(summary.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL")));		
	}
	
	@Test
	public void shouldTrackAnnotatedRankedTypes() throws Exception {
		Qualitative summary = new Qualitative();
		
		new Benchmark(new AnnotationAlgorithmTestDouble().thatReturns("actual predicate"))
						.on(new GoldStandardGroup[]{
								new GoldStandardGroup(new FileSystemConnectorTestDouble().withName("amazon_category_expected relation"))
							}, summary);
		
		assertThat(summary.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL\ncategory|expected relation|actual predicate")));
	}
}