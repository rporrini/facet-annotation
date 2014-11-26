package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedOccurrences;
import it.disco.unimib.labeller.index.SimilarityMetricWrapper;

import org.junit.Test;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaccardSimilarity;

public class ContextualizedOccurrencesTest {

	@Test
	public void shouldEvalutateTheSimilarityBetweenTheGivenDomainAndTheContextOfTheTriple() {
		
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(new SimilarityMetricWrapper(new JaccardSimilarity()));
		
		occurrences.accumulate("predicate", "movie genre", "genre", null, null);
		
		assertThat(occurrences.toResults().get(0).score(), equalTo(0.5));
	}
	
	@Test
	public void shouldSumUpTheOccurrences() {
		
		ContextualizedOccurrences occurrences = new ContextualizedOccurrences(new ConstantSimilarity());
		
		occurrences.accumulate("predicate", "movie genre", "genre", null, null);
		occurrences.accumulate("predicate", "movie genre", "genre", null, null);
		
		assertThat(occurrences.toResults().get(0).score(), equalTo(2.0));
	}
}
