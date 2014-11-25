package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.LiteralObject;
import it.disco.unimib.labeller.index.NTriple;

import org.junit.Test;
import org.semanticweb.yars.nx.parser.NxParser;

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
	
	@Test
	public void integrationTest() throws Exception {
		String notALiteral = "<http://musicbrainz.org/area/424c1b50-57f8-34af-ab97-313e2ad40058#_> <http://www.w3.org/2002/07/owl#sameAs> <http://ontologi.es/place/RS> .";
		
		NTriple triple = new NTriple(NxParser.parseNodes(notALiteral));
		
		assertThat(new LiteralObject().matches(triple), is(false));
	}
}
