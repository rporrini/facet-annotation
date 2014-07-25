package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.MandatoryContext;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.SpecificNamespace;
import it.disco.unimib.labeller.index.TripleIndex;

import java.util.List;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class EvidenceTest {
	
	private final KnowledgeBase yago = new KnowledgeBase("yago1");
	private final KnowledgeBase dbpedia = new KnowledgeBase("dbpedia");

	@Test
	public void shouldIndexTheEntireUriOfTheObjectIfTheKnowledgeBaseIsDBPedia() throws Exception {
		TripleIndex labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
																		.withSubject("http://paris")
																		.withLiteral("the city of paris")
																		.asTriple())
															.closeWriter();
		
		TripleIndex index = new Evidence(new RAMDirectory(), new EntityValues(new RAMDirectory()).closeWriter(), labels, new RankByFrequency(), new OptionalContext(), dbpedia)
							.add(new TripleBuilder()
										.withSubject("http://france")
										.withPredicate("http://hasCapital")
										.withLiteral("http://paris")
										.asTriple())
							.closeWriter();
		
		assertThat(index.get("city", "any").get(0).value(), equalTo("http://hasCapital"));
	}
	
	@Test
	public void shouldIndexTheLabelOfTheObjectIfTheOIbjectIsNotALiteralAndKnowledgeBaseIsYago() throws Exception {
		TripleIndex labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
																		.withSubject("http://paris")
																		.withLiteral("the city of paris")
																		.asTriple())
															.closeWriter();
		
		TripleIndex index = new Evidence(new RAMDirectory(), new EntityValues(new RAMDirectory()).closeWriter(), labels, new RankByFrequency(), new OptionalContext(), yago)
							.add(new TripleBuilder()
										.withSubject("http://france")
										.withPredicate("http://hasCapital")
										.withLiteral("http://paris")
										.asTriple())
							.closeWriter();
		
		assertThat(index.get("city", "any").get(0).value(), equalTo("hasCapital"));
	}
	
	@Test
	public void simpleLiteralsShouldBeSearchableInYago() throws Exception {
		TripleIndex index = new Evidence(new RAMDirectory(), new EntityValues(new RAMDirectory()).closeWriter(), new EntityValues(new RAMDirectory()).closeWriter(), new RankByFrequency(), new OptionalContext(), yago)
							.add(new TripleBuilder().withPredicate("http://property").withLiteral("the literal").asTriple()).closeWriter();
		
		CandidatePredicate searchResult = index.get("literal", "any").get(0);
		
		assertThat(searchResult.value(), equalTo("property"));
		assertThat(searchResult.score(), equalTo(1.0));
	}
	
	@Test
	public void theTypeOfTheSubjectShouldBeSearchableAsContextAndProvideMoreDetailedRanking() throws Exception {
		TripleIndex labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("the type label").asTriple()).closeWriter();
		TripleIndex types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		TripleIndex dbpediaIndex = new Evidence(new RAMDirectory(), types, labels, new RankByFrequency(), new OptionalContext(), dbpedia)
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
		
		TripleIndex yagoIndex = new Evidence(new RAMDirectory(), types, labels, new RankByFrequency(), new OptionalContext(), yago)
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
		
		assertThat(dbpediaIndex.get("literal", "type").get(0).value(), equalTo("http://property"));
		assertThat(yagoIndex.get("literal", "type").get(0).value(), equalTo("property"));
	}
	
	@Test
	public void shouldGroupByPredicate() throws Exception {
		TripleIndex index = new Evidence(new RAMDirectory(), 
										 new EntityValues(new RAMDirectory()).closeWriter(), 
										 new EntityValues(new RAMDirectory()).closeWriter(),
										 new RankByFrequency(), new OptionalContext(), new KnowledgeBase("anyKnowledgeBase"))
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
		TripleIndex labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("plural types").asTriple()).closeWriter();
		TripleIndex types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		TripleIndex index = new Evidence(new RAMDirectory(), types, labels,new RankByFrequency(), new MandatoryContext(), new KnowledgeBase("anyKnowledgeBase"))
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
		
		new Evidence(new RAMDirectory(), null, null, null, new OptionalContext(), new KnowledgeBase("anyKnowledgeBase")).closeWriter().get("a query with a special & character!", "any");
	}
	
	@Test
	public void shouldIndexAndFilterByNamespace() throws Exception {
		TripleIndex types = new EntityValues(new RAMDirectory()).closeWriter();
		TripleIndex labels = new EntityValues(new RAMDirectory()).closeWriter();
		TripleIndex index = new Evidence(new RAMDirectory(), types, labels, new RankByFrequency(), new SpecificNamespace("http://namespace/", new OptionalContext()), new KnowledgeBase("anyKnowledgeBase"))
								.add(new TripleBuilder()
											.withPredicate("http://namespace/property")
											.withLiteral("value")
											.asTriple())
								.closeWriter();
		
		List<CandidatePredicate> results = index.get("value", "any");
		
		assertThat(results, is(not(empty())));
	}
}
