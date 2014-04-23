package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.TrecEval;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.Arrays;

import org.junit.Test;

public class TrecEvalTest {

	@Test
	public void onEmptyContentShouldPrintNothing() {
		TrecEval trecEval = new TrecEval("name");
		
		assertThat(trecEval.result(), equalTo(""));
	}
	
	@Test
	public void shouldTrackTheIdOfTheGroup() throws Exception {
		TrecEval trecEval = new TrecEval("name");
		
		trecEval.track(
				new GoldStandardTestDouble().withGroup("domain_provider_context_label").getGroups()[0], Arrays.asList(
				new AnnotationResult[]{new AnnotationResult("value", 1)}));
		
		assertThat(trecEval.result(), containsString("1761928305"));
	}
	
	@Test
	public void shouldTrackAllTheResults() throws Exception {
		TrecEval trecEval = new TrecEval("name");
		
		trecEval.track(
				new GoldStandardTestDouble().withGroup("domain_provider_context_label").getGroups()[0], Arrays.asList(
				new AnnotationResult[]{new AnnotationResult("value1", 1),
									   new AnnotationResult("value2", 1)}));
		
		assertThat(trecEval.result(), allOf(containsString("value1"), containsString("value2")));
	}
}
