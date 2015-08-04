package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class CandidatePropertyTest {

	@Test
	public void shouldBeOrderedDescending() {
		CandidateProperty resultWithHigherScore = new CandidateProperty("any");
		resultWithHigherScore.sumScore(10);
		CandidateProperty resultWithLowerScore = new CandidateProperty("any");
		resultWithLowerScore.sumScore(5);
		
		ArrayList<CandidateProperty> results = new ArrayList<CandidateProperty>();
		results.add(resultWithLowerScore);
		results.add(resultWithHigherScore);
		
		Collections.sort(results);
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldDisplayTheLocalScoresOnToString() throws Exception {
		
		CandidateProperty property = new CandidateProperty("value");
		
		property.sumScore(0.4);
		
		assertThat(property.toString(), containsString("0.4"));
	}
	
	@Test
	public void whenCreatedShouldHaveTheScoreSetToZero() throws Exception {
		
		CandidateProperty property = new CandidateProperty("any");
		
		assertThat(property.score(), equalTo(0.0));
	}
	
	@Test
	public void shouldIncrementScoreBySum() throws Exception {
		CandidateProperty property = new CandidateProperty("any");
		
		property.sumScore(10.0);
		
		assertThat(property.score(), equalTo(10.0));
	}
	
	@Test
	public void shouldIncrementScoreBySumMultipleTimes() throws Exception {
		CandidateProperty property = new CandidateProperty("any");
		
		property.sumScore(10.0);
		property.sumScore(10.0);
		
		assertThat(property.score(), equalTo(20.0));
	}
	
	@Test
	public void shouldMultiplyTheScores() throws Exception {
		CandidateProperty property = new CandidateProperty("any");
		
		property.multiplyScore(10.0);
		property.multiplyScore(10.0);
		
		assertThat(property.score(), equalTo(100.0));
	}
	
	@Test
	public void shouldCollectSubjectTypes() throws Exception {
		
		CandidateProperty property = new CandidateProperty("any");
		
		property.addDomains("http://subject-type");
		
		assertThat(property.domains().all(), hasSize(1));
	}
	
	@Test
	public void shouldCountOccurrencesOfSubjectTypes() throws Exception {
		CandidateProperty property = new CandidateProperty("any");
		
		property.addDomains("http://subject-type");
		property.addDomains("http://subject-type");
		
		assertThat(property.domains().typeOccurrence("http://subject-type"), equalTo(2.0));
	}
	
	@Test
	public void shouldCollectManySubjectTypesAtTheTime() throws Exception {
		CandidateProperty property = new CandidateProperty("any");
		
		property.addDomains("http://subject-type", "http://other-type");
		
		assertThat(property.domains().all(), hasSize(2));
	}
	
	@Test
	public void shouldCollectManyObjectTypesAtTheTime() throws Exception {
		CandidateProperty property = new CandidateProperty("any");
		
		property.addRanges("http://object-type", "http://object-type");
		
		assertThat(property.ranges().all(), hasSize(1));
	}
	
	@Test
	public void whenCreatedIsNotOccurredYet() throws Exception {
		
		CandidateProperty property = new CandidateProperty("any");
		
		assertThat(property.totalOccurrences(), equalTo(0.0));
	}
	
	@Test
	public void shouldTrackOccurrencesNotOccurredYet() throws Exception {
		
		CandidateProperty property = new CandidateProperty("any");
		
		property.occurred();
		
		assertThat(property.totalOccurrences(), equalTo(1.0));
	}
}
