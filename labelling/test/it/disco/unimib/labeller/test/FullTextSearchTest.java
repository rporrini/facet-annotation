package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.FullTextSearch;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KeyValueStore;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.SpecificNamespace;

import java.util.List;

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
		
		Index index = new FullTextSearch(new RAMDirectory(), new KeyValueStore(new RAMDirectory()).closeWriter(), labels, new RankByFrequency(), new OptionalContext())
							.add(new TripleBuilder()
										.withSubject("http://france")
										.withPredicate("http://hasCapital")
										.withLiteral("http://paris")
										.asTriple())
							.closeWriter();
		
		assertThat(index.get("city", "any").get(0).value(), equalTo("hasCapital"));
	}
	
	@Test
	public void simpleLiteralsShouldBeSearchable() throws Exception {
		Index index = new FullTextSearch(new RAMDirectory(), new KeyValueStore(new RAMDirectory()).closeWriter(), new KeyValueStore(new RAMDirectory()).closeWriter(), new RankByFrequency(), new OptionalContext())
							.add(new TripleBuilder().withPredicate("http://property").withLiteral("the literal").asTriple()).closeWriter();
		
		AnnotationResult searchResult = index.get("literal", "any").get(0);
		
		assertThat(searchResult.value(), equalTo("property"));
		assertThat(searchResult.score(), equalTo(1.0));
	}
	
	@Test
	public void theTypeOfTheSubjectShouldBeSearchableAsContextAndProvideMoreDetailedRanking() throws Exception {
		Index labels = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("the type label").asTriple()).closeWriter();
		Index types = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		Index index = new FullTextSearch(new RAMDirectory(), types, labels, new RankByFrequency(), new OptionalContext())
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
		
		assertThat(index.get("literal", "type").get(0).value(), equalTo("property"));
	}
	
	@Test
	public void shouldGroupByPredicate() throws Exception {
		Index index = new FullTextSearch(new RAMDirectory(), 
										 new KeyValueStore(new RAMDirectory()).closeWriter(), 
										 new KeyValueStore(new RAMDirectory()).closeWriter(),
										 new RankByFrequency(), new OptionalContext())
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
	
	@Test
	public void theContextShouldBeStemmedForEnglish() throws Exception {
		Index labels = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("plural types").asTriple()).closeWriter();
		Index types = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		Index index = new FullTextSearch(new RAMDirectory(), types, labels,new RankByFrequency(), new MandatoryContext())
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withPredicate("http://property")
										.withLiteral("literal")
										.asTriple())
							.closeWriter();
		
		assertThat(index.get("literals", "type"), hasSize(1));
	}
	
	@Test
	public void shouldBeRobustToSpecialCharacters() throws Exception {
		
		new FullTextSearch(new RAMDirectory(), null, null, null, new OptionalContext()).closeWriter().get("a query with a special & character!", "any");
	}
	
	@Test
	public void shouldIndexAndFilterByNamespace() throws Exception {
		Index types = new KeyValueStore(new RAMDirectory()).closeWriter();
		Index labels = new KeyValueStore(new RAMDirectory()).closeWriter();
		Index index = new FullTextSearch(new RAMDirectory(), types, labels, new RankByFrequency(), new SpecificNamespace("http://namespace/", new OptionalContext()))
								.add(new TripleBuilder()
											.withPredicate("http://namespace/property")
											.withLiteral("value")
											.asTriple())
								.closeWriter();
		
		List<AnnotationResult> results = index.get("value", "any");
		
		assertThat(results, is(not(empty())));
	}
}
