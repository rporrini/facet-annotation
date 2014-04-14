package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.FullTextSearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KeyValueStore;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class FullTextSearchTest {

	@Test
	public void simpleLiteralsShouldBeSearchable() throws Exception {
		Index index = new FullTextSearch(new RAMDirectory(), new KeyValueStore(new RAMDirectory()).close(), new KeyValueStore(new RAMDirectory()).close())
							.add(new TripleBuilder().withPredicate("http://property").withLiteral("the literal").asTriple()).close();
		
		assertThat(index.get("literal", "any"), hasItem("http://property"));		
	}
	
	@Test
	public void theTypeOfTheSubjectShouldBeSearchableAsContext() throws Exception {
		Index labels = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("the type label").asTriple()).close();
		Index types = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).close();
		
		Index index = new FullTextSearch(new RAMDirectory(), types, labels)
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withPredicate("http://property")
										.withLiteral("the literal")
										.asTriple())
							.close();
		
		assertThat(index.get("any", "type"), hasItem("http://property"));
	}
}
