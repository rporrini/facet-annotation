package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Qualitative;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.index.CandidatePredicate;

import java.util.Arrays;

import org.junit.Test;

public class QualitativeTest {

	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Summary metric = new Qualitative();
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL")));
	}
	
	@Test
	public void shouldTrackTheExecution() throws Exception {
		Summary metric = new Qualitative();
		
		metric.track(createGroup("provider_context_year"), Arrays.asList(new CandidatePredicate[]{new CandidatePredicate("year", 1)}))
			  .track(createGroup("provider_context_decade"), Arrays.asList(new CandidatePredicate[]{new CandidatePredicate("year", 1)}));
		
		assertThat(metric.result(), is(equalTo("Qualitative analysis\nCONTEXT|EXPECTED|ACTUAL\ncontext|year|year\ncontext|decade|year")));
	}

	private GoldStandardGroup createGroup(String name) {
		GoldStandardGroup year = new GoldStandardGroup(new InputFileTestDouble().withName(name));
		return year;
	}
}
