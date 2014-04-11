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
									new TripleBuilder().withSubject("http://any").withPredicate("http://any").withLiteral("label").asNTriple()))
					.fill(index, new AcceptAll());
		index.close();
		
		assertThat(index.get("http://any"), is(not(empty())));
	}
	
	@Test
	public void shouldAddOnlyMatchingPredicates() throws Exception {
		Index index = new Index(new RAMDirectory());
		new Triples(new FileSystemConnectorTestDouble()
							.withLine(new TripleBuilder().withSubject("http://france").withPredicate("http://label").withLiteral("italy").asNTriple())
							.withLine(new TripleBuilder().withSubject("http://france").withPredicate("http://type").withLiteral("country").asNTriple()))
					.fill(index, new MatchingPredicate("http://label"));
		index.close();
		
		assertThat(index.get("http://france"), hasSize(1));
	}
	
	@Test(timeout = 1000)
	public void shouldSkipOnErrors() throws Exception {
		new Triples(new FileSystemConnectorTestDouble()
							.withLine("曲：[http://musicbrainz.org/artist/a223958d-5c56-4b2c-a30a-87e357bc121b.html|周杰倫]"))
					.fill(new Index(new RAMDirectory()), new AcceptAll());
	}
}