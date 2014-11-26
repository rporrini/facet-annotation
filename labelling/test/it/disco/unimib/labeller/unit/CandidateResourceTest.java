package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
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
		
		CandidateResource predicate = new CandidateResource("value");
		
		predicate.sumScore(0.4);
		
		assertThat(predicate.toString(), containsString("0.4"));
	}
	
	@Test
	public void whenCreatedShouldHaveTheScoreSetToZero() throws Exception {
		
		CandidateResource predicate = new CandidateResource("any");
		
		assertThat(predicate.score(), equalTo(0.0));
	}
	
	@Test
	public void shouldIncrementScoreBySum() throws Exception {
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.sumScore(10.0);
		
		assertThat(predicate.score(), equalTo(10.0));
	}
	
	@Test
	public void shouldIncrementScoreBySumMultipleTimes() throws Exception {
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.sumScore(10.0);
		predicate.sumScore(10.0);
		
		assertThat(predicate.score(), equalTo(20.0));
	}
}
