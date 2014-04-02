package it.disco.unimib.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import it.disco.unimib.benchmark.Metric;
import it.disco.unimib.benchmark.Qualitative;

import org.junit.Test;

public class QualitativeTest {

	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Metric metric = new Qualitative();
		
		assertThat(metric.result(), is(equalTo("EXPECTED|ACTUAL")));
	}
	
	@Test
	public void shouldTrackTheExecution() throws Exception {
		Metric metric = new Qualitative();
		
		metric.track("year", "year")
			  .track("decade", "year");
		
		assertThat(metric.result(), is(equalTo("EXPECTED|ACTUAL\nyear|year\ndecade|year")));
	}
}
