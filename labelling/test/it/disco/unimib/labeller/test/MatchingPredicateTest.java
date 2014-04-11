package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.MatchingPredicate;

import org.junit.Test;

import com.hp.hpl.jena.graph.Triple;

public class MatchingPredicateTest {

	@Test
	public void shouldMatchTripleWithASpecificPredicate() {
		Triple triple = new TripleBuilder().withPredicate("http://the.predicate").asTriple();
		
		boolean matches = new MatchingPredicate("http://the.predicate").matches(triple);
		
		assertThat(matches, is(true));
	}
	
	@Test
	public void shouldNotMatchATripleIfDoesNotContainTheSpecifiedPredicate() throws Exception {
		Triple triple = new TripleBuilder().withPredicate("http://the.predicate").asTriple();
		
		boolean matches = new MatchingPredicate("http://another.predicate").matches(triple);
		
		assertThat(matches, is(false));
	}
}
