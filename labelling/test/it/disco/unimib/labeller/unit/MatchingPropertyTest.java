package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.MatchingProperty;
import it.disco.unimib.labeller.index.NTriple;

import org.junit.Test;

public class MatchingPropertyTest {

	@Test
	public void shouldMatchTripleWithASpecificPredicate() throws Exception {
		NTriple triple = new TripleBuilder().withProperty("http://the.predicate").asTriple();
		
		boolean matches = new MatchingProperty("http://the.predicate").matches(triple);
		
		assertThat(matches, is(true));
	}
	
	@Test
	public void shouldNotMatchATripleIfDoesNotContainTheSpecifiedPredicate() throws Exception {
		NTriple triple = new TripleBuilder().withProperty("http://the.predicate").asTriple();
		
		boolean matches = new MatchingProperty("http://another.predicate").matches(triple);
		
		assertThat(matches, is(false));
	}
}
