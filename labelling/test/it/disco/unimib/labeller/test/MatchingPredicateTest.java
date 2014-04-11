package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.MatchingPredicate;
import it.disco.unimib.labeller.index.NTriple;

import org.junit.Test;

public class MatchingPredicateTest {

	@Test
	public void shouldMatchTripleWithASpecificPredicate() throws Exception {
		NTriple triple = new TripleBuilder().withPredicate("http://the.predicate").asTriple();
		
		boolean matches = new MatchingPredicate("http://the.predicate").matches(triple);
		
		assertThat(matches, is(true));
	}
	
	@Test
	public void shouldNotMatchATripleIfDoesNotContainTheSpecifiedPredicate() throws Exception {
		NTriple triple = new TripleBuilder().withPredicate("http://the.predicate").asTriple();
		
		boolean matches = new MatchingPredicate("http://another.predicate").matches(triple);
		
		assertThat(matches, is(false));
	}
}
