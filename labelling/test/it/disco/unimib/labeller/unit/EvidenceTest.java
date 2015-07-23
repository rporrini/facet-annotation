package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.index.PartiallyContextualizedValue;

import java.util.Collection;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Ignore;
import org.junit.Test;

public class EvidenceTest {
	
	private final IndexFields yago = new IndexFields("yago1");
	private final IndexFields dbpedia = new IndexFields("dbpedia");

	@Test
	@Ignore
	public void shouldIndexLiteralTypesAsObjects() throws Exception {
		
		EntityValues labels = new EntityValues(new RAMDirectory()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
																			.withSubject("http://entity")
																			.withLiteral("http://type")
																			.asTriple()).closeWriter();
		
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, types, labels, dbpedia)
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withProperty("http://property")
										.withTypedLiteral("12", "integer")
										.asTriple())
							.closeWriter();
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"12"});
		OnlyValue query = new OnlyValue(dbpedia);
		
		Collection<CandidateResource> result = new ContextualizedEvidence(directory, new ConstantSimilarity(), dbpedia).get(request, query.asQuery(request)).asList();
		
		assertThat(result.iterator().next().objectTypes(), hasItem(new CandidateResource("integer")));
	}
	
	@Test
	public void shouldIndexTheEntireUriOfTheObjectIfTheKnowledgeBaseIsDBPedia() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
																		.withSubject("http://paris")
																		.withLiteral("the city of paris")
																		.asTriple())
															.closeWriter();
		
		new Evidence(directory, new EntityValues(new RAMDirectory()).closeWriter(), labels, dbpedia)
							.add(new TripleBuilder()
										.withSubject("http://france")
										.withProperty("http://hasCapital")
										.withLiteral("http://paris")
										.asTriple())
							.closeWriter();
		
		ContextualizedEvidence search = new ContextualizedEvidence(directory, new ConstantSimilarity(), dbpedia);
		
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"city"});
		OnlyValue query = new OnlyValue(dbpedia);
		
		assertThat(search.get(request, query.asQuery(request))
								.asList()
								.iterator()
								.next()
								.id(), equalTo("http://hasCapital"));
	}
	
	@Test
	public void simpleLiteralsShouldBeSearchableInYago() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		new Evidence(directory, 
								new EntityValues(new RAMDirectory()).closeWriter(), 
								new EntityValues(new RAMDirectory()).closeWriter(), 
								yago)
							.add(new TripleBuilder().withProperty("http://property").withLiteral("the literal").asTriple()).closeWriter();
		
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"literal"});
		OnlyValue query = new OnlyValue(yago);
		CandidateResource searchResult = new ContextualizedEvidence(directory, new ConstantSimilarity(), yago)
										.get(request, 
											 query.asQuery(request))
										.asList().iterator().next();
		
		assertThat(searchResult.id(), equalTo("property"));
		assertThat(searchResult.score(), equalTo(1.0));
	}
	
	@Test
	public void theTypeOfTheSubjectShouldBeSearchableAsContextAndProvideMoreDetailedRanking() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("the type label").asTriple()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		RAMDirectory dbpediaDirectory = new RAMDirectory();
		new Evidence(dbpediaDirectory, types, labels, dbpedia)
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withProperty("http://property")
										.withLiteral("literal")
										.asTriple())
							.add(new TripleBuilder()
										.withSubject("http://another_entity")
										.withProperty("http://another_property")
										.withLiteral("other literal")
										.asTriple())
							.closeWriter();
		
		ContextualizedValues request = new ContextualizedValues("type", new String[]{"literal"});
		PartiallyContextualizedValue query = new PartiallyContextualizedValue(dbpedia);
		Collection<CandidateResource> results = new ContextualizedEvidence(dbpediaDirectory, new ConstantSimilarity(), dbpedia)
												.get(request, query.asQuery(request))
												.asList();
		
		assertThat(results.iterator().next().id(), equalTo("http://property"));
		
		new Evidence(new RAMDirectory(), types, labels, yago)
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withProperty("http://property")
										.withLiteral("literal")
										.asTriple())
							.add(new TripleBuilder()
										.withSubject("http://another_entity")
										.withProperty("http://another_property")
										.withLiteral("other literal")
										.asTriple())
							.closeWriter();
		
		results = new ContextualizedEvidence(dbpediaDirectory, new ConstantSimilarity(), yago)
							.get(request, query.asQuery(request))
							.asList();
		
		assertThat(results.iterator().next().id(), equalTo("property"));
	}
	
	@Test
	public void theContextShouldBeStemmedForEnglish() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("plural types").asTriple()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, types, labels, new IndexFields("anyKnowledgeBase"))
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withProperty("http://property")
										.withLiteral("literal")
										.asTriple())
							.closeWriter();
		
		ContextualizedValues request = new ContextualizedValues("type", new String[]{"literals"});
		PartiallyContextualizedValue query = new PartiallyContextualizedValue(dbpedia);
		assertThat(new ContextualizedEvidence(directory, new ConstantSimilarity(), dbpedia)
						.get(request, query.asQuery(request))
						.asList(), 
				   hasSize(1));
	}
}
