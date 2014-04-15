package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.LiteralObject;
import it.disco.unimib.labeller.index.NTriple;

import org.junit.Test;

public class LiteralObjectTest {

	@Test
	public void shouldAcceptLiterals() throws Exception {
		NTriple triple = new TripleBuilder().withLiteral("the literal").asTriple();
		
		assertThat(new LiteralObject().matches(triple), is(true));
	}
	
	@Test
	public void shouldNotAcceptTriplesWithEntityObjects() throws Exception {
		NTriple triple = new TripleBuilder().withObject("http://object").asTriple();
		
		assertThat(new LiteralObject().matches(triple), is(false));
	}
}
