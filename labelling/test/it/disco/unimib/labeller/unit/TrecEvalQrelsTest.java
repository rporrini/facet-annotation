package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.TrecEvalQrels;
import it.disco.unimib.labeller.index.CandidateResource;

import java.util.Arrays;

import org.junit.Test;

public class TrecEvalQrelsTest {

	@Test
	public void onEmptyContentShouldPrintNothing() {
		TrecEvalQrels trecEval = new TrecEvalQrels("name");
		
		assertThat(trecEval.result(), equalTo(""));
	}
	
	@Test
	public void shouldTrackTheIdOfTheGroup() throws Exception {
		TrecEvalQrels trecEval = new TrecEvalQrels("name");
		
		trecEval.track(
				new GoldStandardTestDouble().withGroup("domain_provider_context_label").getFacets()[0], Arrays.asList(
				new CandidateResource[]{new CandidateResource("value", 1)}));
		
		assertThat(trecEval.result(), containsString("1761928305"));
	}
	
	@Test
	public void shouldTrackAllTheResults() throws Exception {
		TrecEvalQrels trecEval = new TrecEvalQrels("name");
		
		trecEval.track(
				new GoldStandardTestDouble().withGroup("domain_provider_context_label").getFacets()[0], Arrays.asList(
				new CandidateResource[]{new CandidateResource("value1", 1),
									   new CandidateResource("value2", 1)}));
		
		assertThat(trecEval.result(), allOf(containsString("value1"), containsString("value2")));
	}
}
