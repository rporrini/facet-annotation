package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.benchmark.Metric;
import it.disco.unimib.labeller.benchmark.Qualitative;

import org.junit.Test;

public class QualitativeTest {

	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Metric metric = new Qualitative();
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL")));
	}
	
	@Test
	public void shouldTrackTheExecution() throws Exception {
		Metric metric = new Qualitative();
		
		metric.track("context", "year", "year")
			  .track("context", "decade", "year");
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL\ncontext|year|year\ncontext|decade|year")));
	}
}
