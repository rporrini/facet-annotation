package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Metric;
import it.disco.unimib.labeller.benchmark.Questionnaire;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QuestionnaireTest {
	
	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Metric metric = new Questionnaire();
		
		assertThat(metric.result(), is(equalTo("")));
	}

	@Test
	public void shouldTrackTheExecution() throws Exception {
		Metric metric = new Questionnaire();
		List<AnnotationResult> annotationResults = Arrays.asList(new AnnotationResult[]{new AnnotationResult("year", 1), new AnnotationResult("date", 1)});
		
		metric.track(createGroup("domain_provider_context_year"), annotationResults)
			  .track(createGroup("domain_provider_context_decade"), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)\nvalue1 value2\nyear\ndate\n\ndomain (context)\nvalue1 value2\nyear\ndate")));
	}
	
	@Test
	public void shouldTrackTheHyperlink() throws Exception {
		Metric metric = new Questionnaire();
		List<AnnotationResult> annotationResults = Arrays.asList(new AnnotationResult[]{});
		
		metric.track(createGroup("domain_amazon_context_year_hyperlink"), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1 value2")));
	}

	private GoldStandardGroup createGroup(String name) {
		return new GoldStandardGroup(new FileSystemConnectorTestDouble().withName(name).withLine("value1").withLine("value2"));
	}
}
