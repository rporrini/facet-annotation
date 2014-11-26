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
	
	@Test
	public void shouldMultiplyTheScores() throws Exception {
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.multiplyScore(10.0);
		predicate.multiplyScore(10.0);
		
		assertThat(predicate.score(), equalTo(100.0));
	}
	
	@Test
	public void shouldDisplayAllTheLocalScoresWhenAdded() throws Exception {
		
		CandidateResource predicate = new CandidateResource("predicate");
		
		predicate.sumScore(2.0);
		predicate.sumScore(3.0);
		
		assertThat(predicate.toString(), containsString("[2.0, 3.0, 5.0]"));
	}
	
	@Test
	public void shouldAllowAddingAndMultiplyingScores() throws Exception {
		CandidateResource predicate = new CandidateResource("predicate");
		
		predicate.sumScore(2.0);
		predicate.multiplyScore(10.0);
		
		assertThat(predicate.toString(), containsString("[2.0, 10.0, 20.0]"));
	}
	
	@Test
	public void shouldCollectSubjectTypes() throws Exception {
		
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.addSubjectTypes("http://subject-type");
		
		assertThat(predicate.subjectTypes(), hasSize(1));
	}
	
	@Test
	public void shouldCountOccurrencesOfSubjectTypes() throws Exception {
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.addSubjectTypes("http://subject-type");
		predicate.addSubjectTypes("http://subject-type");
		
		assertThat(predicate.subjectTypes().get(0).score() , equalTo(2.0));
	}
	
	@Test
	public void shouldCollectManySubjectTypesAtTheTime() throws Exception {
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.addSubjectTypes("http://subject-type", "http://other-type");
		
		assertThat(predicate.subjectTypes() , hasSize(2));
	}
	
	@Test
	public void shouldCollectManyObjectTypesAtTheTime() throws Exception {
		CandidateResource predicate = new CandidateResource("any");
		
		predicate.addObjectTypes("http://object-type", "http://object-type");
		
		assertThat(predicate.objectTypes() , hasSize(1));
	}
}
