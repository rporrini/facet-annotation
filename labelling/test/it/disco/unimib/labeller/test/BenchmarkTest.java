package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Qualitative;
import it.disco.unimib.labeller.labelling.AnnotationWithType;

import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void onEmptyGoldStandardShouldTrackNoting() throws Exception {
		Qualitative summary = new Qualitative();
		
		new Benchmark(new AnnotationWithType(null, null)).on(new GoldStandardGroup[]{}, summary);
		
		assertThat(summary.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL")));		
	}
	
	@Test
	public void shouldTrackAnnotatedRankedTypes() throws Exception {
		Qualitative summary = new Qualitative();
		
		new Benchmark(new AnnotationWithType(new AnnotatorTestDouble(), new TypeRankerTestDouble().thatReturns("actual type")))
						.on(new GoldStandardGroup[]{
								new GoldStandardGroup(new FileSystemConnectorTestDouble().withName("ecommerce_amazon_category_expected type"))
							}, summary);
		
		assertThat(summary.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL\necommerce|category|expected type|actual type")));
	}
}