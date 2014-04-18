package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.FullTextSearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KeyValueStore;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class FullTextSearchTest {

	@Test
	public void shouldIndexTheLabelOfTheObjectIfTheOIbjectIsNotALiteral() throws Exception {
		Index labels = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder()
																		.withSubject("http://paris")
																		.withLiteral("the city of paris")
																		.asTriple())
															.closeWriter();
		
		Index index = new FullTextSearch(new RAMDirectory(), new KeyValueStore(new RAMDirectory()).closeWriter(), labels)
							.add(new TripleBuilder()
										.withSubject("http://france")
										.withPredicate("http://hasCapital")
										.withLiteral("http://paris")
										.asTriple())
							.closeWriter();
		
		assertThat(index.get("city", "any").get(0), equalTo("http://hasCapital"));
	}
	
	@Test
	public void simpleLiteralsShouldBeSearchable() throws Exception {
		Index index = new FullTextSearch(new RAMDirectory(), new KeyValueStore(new RAMDirectory()).closeWriter(), new KeyValueStore(new RAMDirectory()).closeWriter())
							.add(new TripleBuilder().withPredicate("http://property").withLiteral("the literal").asTriple()).closeWriter();
		
		assertThat(index.get("literal", "any"), hasItem("http://property"));		
	}
	
	@Test
	public void theTypeOfTheSubjectShouldBeSearchableAsContextAndProvideMoreDetailedRanking() throws Exception {
		Index labels = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("the type label").asTriple()).closeWriter();
		Index types = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		Index index = new FullTextSearch(new RAMDirectory(), types, labels)
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withPredicate("http://property")
										.withLiteral("literal")
										.asTriple())
							.add(new TripleBuilder()
										.withSubject("http://another_entity")
										.withPredicate("http://another_property")
										.withLiteral("other literal")
										.asTriple())
							.closeWriter();
		
		assertThat(index.get("literal", "type").get(0), equalTo("http://property"));
	}
	
	@Test
	public void shouldGroupByPredicate() throws Exception {
		Index index = new FullTextSearch(new RAMDirectory(), 
										 new KeyValueStore(new RAMDirectory()).closeWriter(), 
										 new KeyValueStore(new RAMDirectory()).closeWriter())
							.add(new TripleBuilder()
										.withPredicate("http://property")
										.withLiteral("the literal")
										.asTriple())
							.add(new TripleBuilder()
										.withPredicate("http://property")
										.withLiteral("another literal")
										.asTriple())
							.add(new TripleBuilder()
										.withPredicate("http://another_property")
										.withLiteral("another literal")
										.asTriple())
						    .closeWriter();
		
		assertThat(index.get("literal", "any"), hasSize(2));
	}
}
