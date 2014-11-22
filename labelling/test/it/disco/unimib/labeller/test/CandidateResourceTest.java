package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.index.CandidateResource;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class CandidateResourceTest {

	@Test
	public void shouldBeOrderedDescending() {
		CandidateResource resultWithHigherScore = new CandidateResource("any", 20);
		CandidateResource resultWithLowerScore = new CandidateResource("any", 10);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		results.add(resultWithLowerScore);
		results.add(resultWithHigherScore);
		
		Collections.sort(results);
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldDisplayTheLocalScoresOnToString() throws Exception {
		
		CandidateResource predicate = new CandidateResource("value", 0.4);
		
		assertThat(predicate.toString(), containsString("0.4"));
	}
	
	@Test
	public void shouldGiveTheRightScore() throws Exception {
		
		CandidateResource predicate = new CandidateResource("value", 0.4);
		
		assertThat(predicate.score(), equalTo(0.4));
	}
	
	@Test
	public void shouldDisplayAllTheLocalScores() throws Exception {
		
		CandidateResource predicate = new CandidateResource("value", 0.45, 0.94, 10);
		
		assertThat(predicate.toString(), allOf(containsString("0.45"), containsString("0.94"), containsString("10")));
	}
	
	@Test
	public void shouldGiveTheLastScoreAsTheCorrectOne() throws Exception {
		
		CandidateResource predicate = new CandidateResource("value", 0.4, 0.3, 10);
		
		assertThat(predicate.score(), equalTo(10d));
	}
}
