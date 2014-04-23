package it.disco.unimib.labeller.test;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.benchmark.Questionnaire;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class QuestionnaireTest {
	
	private List<AnnotationResult> annotationResults = new ArrayList<AnnotationResult>();
	private Summary metric = new Questionnaire();
	
	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Summary metric = new Questionnaire();
		
		assertThat(metric.result(), is(equalTo("")));
	}

	@Test
	public void shouldTrackTheExecution() throws Exception {
		metric.track(createGroup("domain_provider_context_year", 2), annotationResults)
			  .track(createGroup("domain_provider_context_decade", 2), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)\nvalue1|value2\n\ndomain (context)\nvalue1|value2")));
	}
	
	@Test
	public void shouldTrackTheHyperlink() throws Exception {
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 2), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1|value2")));
	}
	
	@Test
	public void shouldTrackFiveGroupValues() throws Exception {		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 5), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1|value2|value3|value4|value5")));
	}
	
	@Test
	public void shouldTrackMoreThanFiveGroupValues() throws Exception {		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 7), annotationResults);
		
		assertThat(metric.result(), is(equalTo("\ndomain (context)|http://www.amazon.com/gp/search/other/hyperlink\nvalue1|value2|value3|value4|value5\nvalue6|value7")));
	}
	
	@Test
	public void shouldTrackPropertyHyperlink() throws Exception {
		annotationResults.add(new AnnotationResult("year", 1));
		annotationResults.add(new AnnotationResult("date", 1));
		
		metric.track(createGroup("domain_amazon_context_label", 0), annotationResults);
		
		assertThat(metric.result(), allOf(containsString("year"), containsString("date"), containsString("dbpedia")));
	}

	private GoldStandardGroup createGroup(String name, int numberOfValues) {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble().withName(name);
		for(int i = 1; i <= numberOfValues; i++){
			connector.withLine("value" + i);
		}
		return new GoldStandardGroup(connector);
	}
}
