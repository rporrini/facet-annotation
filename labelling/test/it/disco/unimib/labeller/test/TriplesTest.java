package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.MatchingPredicate;
import it.disco.unimib.labeller.index.Triples;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;


public class TriplesTest {

	@Test
	public void shouldFillTheIndex() throws Exception {
		Index index = new Index(new RAMDirectory());
		
		new Triples(new FileSystemConnectorTestDouble().withLine(
									new TripleBuilder().withSubject("<http://any>").withPredicate("<http://any>").withLiteral("label").asNTriple()))
					.fill(index, new AcceptAll());
		
		assertThat(index.get("http://any"), is(not(empty())));
	}
	
	@Test
	public void shouldAddOnlyMatchingPredicates() throws Exception {
		Index index = new Index(new RAMDirectory());
		
		new Triples(new FileSystemConnectorTestDouble()
							.withLine(new TripleBuilder().withSubject("<http://france>").withPredicate("<http://label>").withLiteral("italy").asNTriple())
							.withLine(new TripleBuilder().withSubject("<http://france>").withPredicate("<http://type>").withLiteral("country").asNTriple()))
					.fill(index, new MatchingPredicate("http://label"));
		
		assertThat(index.get("http://france"), hasSize(1));
	}
}