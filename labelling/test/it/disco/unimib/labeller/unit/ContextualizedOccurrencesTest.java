package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedOccurrences;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;

import org.junit.Test;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class ContextualizedOccurrencesTest {

	@Test
	public void shouldEvalutateTheSimilarityBetweenTheGivenDomainAndTheContextOfTheTriple() {
		
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(
													new SimilarityMetricWrapper(new JaccardSimilarity()),
													"genre");
		
		occurrences.accumulate("predicate", "movie genre", new String[]{}, new String[]{});
		
		assertThat(occurrences.asResults().asList().iterator().next().score(), equalTo(0.5));
	}
	
	@Test
	public void shouldSumUpTheOccurrences() {
		
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(
													new ConstantSimilarity(),
													"any");
		
		occurrences.accumulate("predicate", "movie genre", new String[]{}, new String[]{});
		occurrences.accumulate("predicate", "movie genre", new String[]{}, new String[]{});
		
		CandidateProperty first = occurrences.asResults().asList().iterator().next();
		assertThat(first.score(), equalTo(2.0));
		assertThat(first.totalOccurrences(), equalTo(2.0));
	}
	
	@Test
	public void shouldAccumulateByPredicate() throws Exception {
		
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(
													new ConstantSimilarity(),
													"any");
		
		occurrences.accumulate("predicate1", "movie genre", new String[]{}, new String[]{});
		occurrences.accumulate("predicate1", "movie genre", new String[]{}, new String[]{});
		occurrences.accumulate("predicate2", "movie genre", new String[]{}, new String[]{});
		
		assertThat(occurrences.asResults().asList(), hasSize(2));
	}
	
	@Test
	public void shouldAccumulateSubjectAndObjectTypes() throws Exception {
		
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(
													new ConstantSimilarity(),
													"any");
		occurrences.accumulate("predicate1", "movie genre", new String[]{
				"subject-type"
		}, new String[]{
				"object-type"
		});
		CandidateProperty candidateResource = occurrences.asResults().asList().iterator().next();
		
		assertThat(candidateResource.domains().all(), hasSize(1));
		assertThat(candidateResource.ranges().all(), hasSize(1));
	}
}
