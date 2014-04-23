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
		
		metric.track(createGroup("domain_provider_context_year", 2), annotationResults)
			  .track(createGroup("domain_provider_context_decade", 2), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)\nvalue1|value2\nyear\ndate\n\ndomain (context)\nvalue1|value2\nyear\ndate")));
	}
	
	@Test
	public void shouldTrackTheHyperlink() throws Exception {
		Metric metric = new Questionnaire();
		List<AnnotationResult> annotationResults = Arrays.asList(new AnnotationResult[]{});
		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 2), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1|value2")));
	}
	
	@Test
	public void shouldTrackFiveGroupValues() throws Exception {
		Metric metric = new Questionnaire();
		List<AnnotationResult> annotationResults = Arrays.asList(new AnnotationResult[]{});
		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 5), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1|value2|value3|value4|value5")));
	}
	
	@Test
	public void shouldTrackMoreThanFiveGroupValues() throws Exception {
		Metric metric = new Questionnaire();
		List<AnnotationResult> annotationResults = Arrays.asList(new AnnotationResult[]{});
		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 7), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1|value2|value3|value4|value5\nvalue6|value7")));
	}

	private GoldStandardGroup createGroup(String name, int numberOfValues) {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble();
		for(int i = 1; i <= numberOfValues; i++){
			connector.withName(name).withLine("value" + i);
		}
		return new GoldStandardGroup(connector);
	}
}
