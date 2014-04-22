package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Metric;
import it.disco.unimib.labeller.benchmark.Questionnaire;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.Arrays;

import org.junit.Test;

public class QuestionnaireTest {
	
	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Metric metric = new Questionnaire();
		
		assertThat(metric.result(), is(equalTo("\nDOMAIN|CONTEXT")));
	}

	@Test
	public void shouldTrackTheExecution() throws Exception {
		Metric metric = new Questionnaire();
		
		metric.track(createGroup("domain_provider_context_year"), Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1)}))
			  .track(createGroup("domain_provider_context_decade"), Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1)}));
		
		assertThat(metric.result(), is(equalTo("\nDOMAIN|CONTEXT\n\ndomain|context\nvalue1 value2\nyear\n\ndomain|context\nvalue1 value2\nyear")));
	}

	private GoldStandardGroup createGroup(String name) {
		GoldStandardGroup year = new GoldStandardGroup(new FileSystemConnectorTestDouble().withName(name).withLine("value1").withLine("value2"));
		return year;
	}
}
