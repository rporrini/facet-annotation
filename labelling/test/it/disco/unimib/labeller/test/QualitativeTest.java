package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Metric;
import it.disco.unimib.labeller.benchmark.Qualitative;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.Arrays;

import org.junit.Test;

public class QualitativeTest {

	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Metric metric = new Qualitative();
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL")));
	}
	
	@Test
	public void shouldTrackTheExecution() throws Exception {
		Metric metric = new Qualitative();
		
		metric.track("domain", "context", "year", Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1)}))
			  .track("domain", "context", "decade", Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1)}));
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL\ndomain|context|year|year\ndomain|context|decade|year")));
	}
}
