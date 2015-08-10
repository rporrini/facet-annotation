package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AcceptAll;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.MatchingProperty;
import it.disco.unimib.labeller.index.Triples;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class TriplesTest {

	@Test
	public void shouldFillTheIndex() throws Exception {
		EntityValues index = new EntityValues(new RAMDirectory());
		new Triples(new InputFileTestDouble().withLine(
									new TripleBuilder().withSubject("http://any").withProperty("http://any").withLiteral("the label").asNTriple()))
					.fill(index, new AcceptAll());
		index.closeWriter();
		
		assertThat(index.get("http://any").get(0).uri(), equalTo("the label"));
	}
	
	@Test
	public void shouldAddOnlyMatchingPredicates() throws Exception {
		EntityValues index = new EntityValues(new RAMDirectory());
		new Triples(new InputFileTestDouble()
							.withLine(new TripleBuilder().withSubject("http://france").withProperty("http://label").withLiteral("italy").asNTriple())
							.withLine(new TripleBuilder().withSubject("http://france").withProperty("http://type").withLiteral("country").asNTriple()))
					.fill(index, new MatchingProperty("http://label"));
		index.closeWriter();
		
		assertThat(index.get("http://france"), hasSize(1));
	}
	
	@Test(timeout = 1000)
	public void shouldSkipStrangeLines() throws Exception {
		new Triples(new InputFileTestDouble()
							.withLine("曲：[http://musicbrainz.org/artist/a223958d-5c56-4b2c-a30a-87e357bc121b.html|周杰倫]"))
					.fill(new EntityValues(new RAMDirectory()), new AcceptAll());
	}
	
	@Test(timeout = 1000)
	public void shouldSkipOtherStrangeLines() throws Exception {
		new Triples(new InputFileTestDouble()
							.withLine("Inaccurate ARs:")
							.withLine("")
							.withLine("    * 混音助理 means \"mixing assistant\", but is credited as \"co-mixer\".\" ."))
					.fill(new EntityValues(new RAMDirectory()), new AcceptAll());
	}
	
	@Test
	public void shouldIndexAlsoWithSpaces() throws Exception {
		EntityValues index = new EntityValues(new RAMDirectory());
		new Triples(new InputFileTestDouble()
							.withLine("<http://1234> <http://predicate> <http://uri with space> ."))
					.fill(index, new AcceptAll());
		index.closeWriter();
		
		assertThat(index.get("http://1234").get(0).uri(), equalTo("http://uri%20with%20space"));
	}
}