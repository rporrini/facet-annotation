package it.disco.unimib.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.index.Index;
import it.disco.unimib.index.Triples;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class TriplesTest {

	@Test
	public void shouldFillTheIndex() throws Exception {
		Index index = new Index(new RAMDirectory());
		
		new Triples(new FileSystemConnectorTestDouble().withLine(
									new TripleBuilder().withSubject("<http://any>").withPredicate("<http://any>").withLiteral("label").asNTriple()))
					.fill(index);
		
		assertThat(index.get("http://any"), is(not(empty())));
	}
}
