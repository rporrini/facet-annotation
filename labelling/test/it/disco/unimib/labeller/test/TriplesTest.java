package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
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
									new TripleBuilder().withSubject("http://any").withPredicate("http://any").withLiteral("the label").asNTriple()))
					.fill(index, new AcceptAll());
		index.close();
		
		assertThat(index.get("http://any"), hasItem("the label"));
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
	public void shouldSkipStrangeLines() throws Exception {
		new Triples(new FileSystemConnectorTestDouble()
							.withLine("曲：[http://musicbrainz.org/artist/a223958d-5c56-4b2c-a30a-87e357bc121b.html|周杰倫]"))
					.fill(new Index(new RAMDirectory()), new AcceptAll());
	}
	
	@Test(timeout = 1000)
	public void shouldSkipOtherStrangeLines() throws Exception {
		new Triples(new FileSystemConnectorTestDouble()
							.withLine("Inaccurate ARs:")
							.withLine("")
							.withLine("    * 混音助理 means \"mixing assistant\", but is credited as \"co-mixer\".\" ."))
					.fill(new Index(new RAMDirectory()), new AcceptAll());
	}
	
	@Test
	public void shouldIndexAlsoWithSpaces() throws Exception {
		Index index = new Index(new RAMDirectory());
		new Triples(new FileSystemConnectorTestDouble()
							.withLine("<http://1234> <http://predicate> <http://uri with space> ."))
					.fill(index, new AcceptAll());
		index.close();
		
		assertThat(index.get("http://1234"), hasItem("http://uri%20with%20space"));
	}
}