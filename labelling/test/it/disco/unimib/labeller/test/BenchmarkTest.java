package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Benchmark;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Metric;
import it.disco.unimib.labeller.benchmark.Qualitative;

import org.junit.Test;

public class BenchmarkTest {

	@Test
	public void onEmptyGoldStandardShouldTrackNoting() throws Exception {
		Qualitative metric = new Qualitative();
		
		new Benchmark(null, null).on(new GoldStandardGroup[]{}, new Metric[]{metric});
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL")));		
	}
	
	@Test
	public void shouldTrackAnnotatedRankedTypes() throws Exception {
		Qualitative metric = new Qualitative();
		
		new Benchmark(new AnnotatorTestDouble(), new TypeRankerTestDouble().thatReturns("actual type"))
						.on(new GoldStandardGroup[]{
								new GoldStandardGroup(new FileSystemConnectorTestDouble().withName("ecommerce_amazon_category_expected type"))
							}, 
							new Metric[]{
								metric
							});
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL\ncategory|expected type|actual type")));
	}
}