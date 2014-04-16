package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.Metric;
import it.disco.unimib.labeller.benchmark.Qualitative;

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
		
		metric.track("domain", "context", "year", "year")
			  .track("domain", "context", "decade", "year");
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nDOMAIN|CONTEXT|EXPECTED|ACTUAL\ndomain|context|year|year\ndomain|context|decade|year")));
	}
}
