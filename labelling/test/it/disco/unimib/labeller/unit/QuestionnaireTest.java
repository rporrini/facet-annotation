package it.disco.unimib.labeller.unit;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.Questionnaire;
import it.disco.unimib.labeller.benchmark.Summary;
import it.disco.unimib.labeller.index.CandidateProperty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class QuestionnaireTest {
	
	private List<CandidateProperty> annotationResults;
	private Summary metric;
	
	@Before
	public void setUp(){
		metric = new Questionnaire();
		annotationResults = new ArrayList<CandidateProperty>();
	}
	
	@Test
	public void onEmptyEvaluationShouldDisplayNothing() {
		Summary metric = new Questionnaire();
		
		assertThat(metric.result(), is(equalTo("")));
	}
	
	@Test
	public void shouldTrackTheExecution() throws Exception {
		metric.track(createGroup("provider_context_label", 1), annotationResults);
		
		assertThat(metric.result(), containsString("context|=HYPERLINK(\"\", \"View context\")"));
	}
	
	@Test
	public void shouldTrackTheExecutionWithLabel() throws Exception {
		annotationResults.add(new CandidateProperty("anyPath/year"));
		metric.track(createGroup("provider_context_label", 1), annotationResults);
		
		assertThat(metric.result(), containsString("|year|"));
	}

	@Test
	public void shouldTrackTheExecutionWithMultipleContexts() throws Exception {
		metric.track(createGroup("provider_context1_year", 2), annotationResults)
			  .track(createGroup("provider_context2_decade", 2), annotationResults);
		
		assertThat(metric.result(), allOf(containsString("context1"), containsString("context2")));
	}
	
	@Test
	public void shouldTrackTheHyperlink() throws Exception {
		metric.track(createGroup("amazon_context_year_hyperlink", 2), annotationResults);
		
		assertThat(metric.result(), allOf(containsString("hyperlink"), containsString("amazon")));
	}
	
	@Test
	public void shouldTrackFiveGroupValues() throws Exception {		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 5), annotationResults);
		
		assertThat(metric.result(), containsString("\nvalue1|value2|value3|value4|value5"));
	}
	
	@Test
	public void shouldTrackMoreThanFiveGroupValues() throws Exception {		
		metric.track(createGroup("domain_amazon_context_year_hyperlink", 7), annotationResults);
		
		assertThat(metric.result(), containsString("\nvalue1|value2|value3|value4|value5\nvalue6|value7"));
	}
	
	@Test
	public void shouldTrackPropertyHyperlink() throws Exception {
		annotationResults.add(new CandidateProperty("year"));
		annotationResults.add(new CandidateProperty("date"));
		
		metric.track(createGroup("amazon_context_label_url", 0), annotationResults);
		
		assertThat(metric.result(), allOf(containsString("=HYPERLINK("),
										  containsString("year"), 
										  containsString("date"), 
										  containsString("dbpedia")));
	}
	
	@Test
	public void shouldPrintTheGroupId() throws Exception {
		metric.track(createGroup("provider_context_label_url", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("873353146"));
	}

	@Test
	public void shouldPrintTheIMDBUrl() throws Exception {
		metric.track(createGroup("IMDB_movies_genre_genre", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("http://www.imdb.com/genre\""));
	}
	
	@Test
	public void shouldPrintTheWikipediaUrl() throws Exception {
		metric.track(createGroup("wikipedia_airports_city served_List_of_airports_in_Italy", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("http://en.wikipedia.org/wiki/List_of_airports_in_Italy\""));
	}
	
	@Test
	public void shouldPrintThePriceGrabberUrl() throws Exception {
		metric.track(createGroup("pricegrabber_tablets and readers_manufacturer", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("http://www.pricegrabber.com/electronics/tablets-e-readers/p-5908/\""));
	}
	
	@Test
	public void shouldPrintTheDiscogsUrl() throws Exception {
		metric.track(createGroup("discogs_music albums_genre", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("http://www.discogs.com/\""));
	}
	
	@Test
	public void shouldPrintTheAmazonUrl() throws Exception {
		metric.track(createGroup("amazon_wines_country_ref=lp_2983386011_sa_p_n_style_browse-bin?rh=n%3A16310101%2Cn%3A!16310211%2Cn%3A2983386011&bbn=2983386011&pickerToList=style_browse-bin&ie=UTF8&qid=1398240361", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("http://www.amazon.com/gp/search/other/ref=lp_2983386011_sa_p_n_style_browse-bin?rh=n%3A16310101%2Cn%3A!16310211%2Cn%3A2983386011&bbn=2983386011&pickerToList=style_browse-bin&ie=UTF8&qid=1398240361\""));
	}
	
	@Test
	public void shouldPrintTheAllStarNBAUrl() throws Exception {
		metric.track(createGroup("allstartnba_basketball players_draft year_players-by-draft-pick.htm", 0), new ArrayList<CandidateProperty>());
		
		assertThat(metric.result(), containsString("http://www.allstarnba.es/players/players-by-draft-pick.htm\""));
	}
	
	private GoldStandardFacet createGroup(String name, int numberOfValues) {
		InputFileTestDouble connector = new InputFileTestDouble().withName(name);
		for(int i = 1; i <= numberOfValues; i++){
			connector.withLine("value" + i);
		}
		return new GoldStandardFacet(connector);
	}
}
