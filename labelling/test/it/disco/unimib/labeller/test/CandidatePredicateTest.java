package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class CandidatePredicateTest {

	@Test
	public void shouldBeOrderedDescending() {
		CandidatePredicate resultWithHigherScore = new CandidatePredicate("any", 20);
		CandidatePredicate resultWithLowerScore = new CandidatePredicate("any", 10);
		
		ArrayList<CandidatePredicate> results = new ArrayList<CandidatePredicate>();
		results.add(resultWithLowerScore);
		results.add(resultWithHigherScore);
		
		Collections.sort(results);
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
}
