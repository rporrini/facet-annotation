package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.benchmark.Qualitative;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.Arrays;

import org.junit.Test;

public class QualitativeTest {

	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Summary metric = new Qualitative();
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL")));
	}
	
	@Test
	public void shouldTrackTheExecution() throws Exception {
		Summary metric = new Qualitative();
		
		metric.track(createGroup("domain_provider_context_year"), Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1)}))
			  .track(createGroup("domain_provider_context_decade"), Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1)}));
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL\ndomain|context|year|year\ndomain|context|decade|year")));
	}

	private GoldStandardGroup createGroup(String name) {
		GoldStandardGroup year = new GoldStandardGroup(new FileSystemConnectorTestDouble().withName(name));
		return year;
	}
}
