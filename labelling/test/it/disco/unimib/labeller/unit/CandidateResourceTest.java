package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class CandidateResourceTest {

	@Test
	public void shouldBeOrderedDescending() {
		CandidateResource resultWithHigherScore = new CandidateResource("any");
		resultWithHigherScore.sumScore(10);
		CandidateResource resultWithLowerScore = new CandidateResource("any");
		resultWithLowerScore.sumScore(5);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		results.add(resultWithLowerScore);
		results.add(resultWithHigherScore);
		
		Collections.sort(results);
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldDisplayTheLocalScoresOnToString() throws Exception {
		
		CandidateResource property = new CandidateResource("value");
		
		property.sumScore(0.4);
		
		assertThat(property.toString(), containsString("0.4"));
	}
	
	@Test
	public void whenCreatedShouldHaveTheScoreSetToZero() throws Exception {
		
		CandidateResource property = new CandidateResource("any");
		
		assertThat(property.score(), equalTo(0.0));
	}
	
	@Test
	public void shouldIncrementScoreBySum() throws Exception {
		CandidateResource property = new CandidateResource("any");
		
		property.sumScore(10.0);
		
		assertThat(property.score(), equalTo(10.0));
	}
	
	@Test
	public void shouldIncrementScoreBySumMultipleTimes() throws Exception {
		CandidateResource property = new CandidateResource("any");
		
		property.sumScore(10.0);
		property.sumScore(10.0);
		
		assertThat(property.score(), equalTo(20.0));
	}
	
	@Test
	public void shouldMultiplyTheScores() throws Exception {
		CandidateResource property = new CandidateResource("any");
		
		property.multiplyScore(10.0);
		property.multiplyScore(10.0);
		
		assertThat(property.score(), equalTo(100.0));
	}
	
	@Test
	public void shouldCollectSubjectTypes() throws Exception {
		
		CandidateResource property = new CandidateResource("any");
		
		property.addDomains("http://subject-type");
		
		assertThat(property.domains(), hasSize(1));
	}
	
	@Test
	public void shouldCountOccurrencesOfSubjectTypes() throws Exception {
		CandidateResource property = new CandidateResource("any");
		
		property.addDomains("http://subject-type");
		property.addDomains("http://subject-type");
		
		assertThat(property.domains().iterator().next().score() , equalTo(2.0));
	}
	
	@Test
	public void shouldCollectManySubjectTypesAtTheTime() throws Exception {
		CandidateResource property = new CandidateResource("any");
		
		property.addDomains("http://subject-type", "http://other-type");
		
		assertThat(property.domains() , hasSize(2));
	}
	
	@Test
	public void shouldCollectManyObjectTypesAtTheTime() throws Exception {
		CandidateResource property = new CandidateResource("any");
		
		property.addRanges("http://object-type", "http://object-type");
		
		assertThat(property.ranges() , hasSize(1));
	}
	
	@Test
	public void whenCreatedIsNotOccurredYet() throws Exception {
		
		CandidateResource property = new CandidateResource("any");
		
		assertThat(property.totalOccurrences(), equalTo(0.0));
	}
	
	@Test
	public void shouldTrackOccurrencesNotOccurredYet() throws Exception {
		
		CandidateResource property = new CandidateResource("any");
		
		property.occurred();
		
		assertThat(property.totalOccurrences(), equalTo(1.0));
	}
}
