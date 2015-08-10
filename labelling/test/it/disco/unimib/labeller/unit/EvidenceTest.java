package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.index.PartiallyContextualizedValue;
import it.disco.unimib.labeller.index.TypeHierarchy;

import java.util.Collection;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class EvidenceTest {
	
	private final IndexFields yago = new IndexFields("yago1");
	private final IndexFields dbpedia = new IndexFields("dbpedia");

	@Test
	public void shouldIndexLiteralTypesAsObjects() throws Exception {
		
		EntityValues labels = new EntityValues(new RAMDirectory()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
																			.withSubject("http://entity")
																			.withLiteral("http://type")
																			.asTriple()).closeWriter();
		
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, new TypeHierarchy(new InputFileTestDouble()), types, labels, dbpedia)
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withProperty("http://property")
										.withTypedLiteral("12", "integer")
										.asTriple())
							.closeWriter();
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"12"});
		OnlyValue query = new OnlyValue(dbpedia);
		
		Collection<CandidateProperty> result = new ContextualizedEvidence(directory, new ConstantSimilarity(), dbpedia).get(request, query.asQuery(request)).asList();
		
		assertThat(result.iterator().next().ranges().all().iterator().next(), equalTo("integer"));
	}
	
	@Test
	public void shouldIndexTheEntireUriOfTheObjectIfTheKnowledgeBaseIsDBPedia() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
																		.withSubject("http://paris")
																		.withLiteral("the city of paris")
																		.asTriple())
															.closeWriter();
		
		new Evidence(directory, new TypeHierarchy(new InputFileTestDouble()), new EntityValues(new RAMDirectory()).closeWriter(), labels, dbpedia)
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
								.uri(), equalTo("http://hasCapital"));
	}
	
	@Test
	public void simpleLiteralsShouldBeSearchableInYago() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		new Evidence(directory, new TypeHierarchy(new InputFileTestDouble()), 
								new EntityValues(new RAMDirectory()).closeWriter(), 
								new EntityValues(new RAMDirectory()).closeWriter(), 
								yago)
							.add(new TripleBuilder().withProperty("http://property").withLiteral("the literal").asTriple()).closeWriter();
		
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"literal"});
		OnlyValue query = new OnlyValue(yago);
		CandidateProperty searchResult = new ContextualizedEvidence(directory, new ConstantSimilarity(), yago)
										.get(request, 
											 query.asQuery(request))
										.asList().iterator().next();
		
		assertThat(searchResult.uri(), equalTo("property"));
		assertThat(searchResult.score(), equalTo(1.0));
	}
	
	@Test
	public void theTypeOfTheSubjectShouldBeSearchableAsContextAndProvideMoreDetailedRanking() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("the type label").asTriple()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		RAMDirectory dbpediaDirectory = new RAMDirectory();
		new Evidence(dbpediaDirectory, new TypeHierarchy(new InputFileTestDouble()), types, labels, dbpedia)
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
		Collection<CandidateProperty> results = new ContextualizedEvidence(dbpediaDirectory, new ConstantSimilarity(), dbpedia)
												.get(request, query.asQuery(request))
												.asList();
		
		assertThat(results.iterator().next().uri(), equalTo("http://property"));
		
		new Evidence(new RAMDirectory(), new TypeHierarchy(new InputFileTestDouble()), types, labels, yago)
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
		
		assertThat(results.iterator().next().uri(), equalTo("property"));
	}
	
	@Test
	public void theContextShouldBeStemmedForEnglish() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("plural types").asTriple()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, new TypeHierarchy(new InputFileTestDouble()), types, labels, new IndexFields("anyKnowledgeBase"))
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
	
	@Test
	public void shouldSaveOnlyMinimalTypesForSubjects() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://person").asTriple())
																  .add(new TripleBuilder().withSubject("http://thing").asTriple())
																		.closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withObject("http://person").asTriple())
																 .add(new TripleBuilder().withSubject("http://entity").withObject("http://thing").asTriple())
																		 .closeWriter();
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble()
																 .withLine(new TripleBuilder()
																 			.withSubject("http://person").withObject("http://thing").asNTriple()));
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, hierarchy, types, labels, new IndexFields("anyKnowledgeBase"))
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withProperty("http://property")
										.withLiteral("literal")
										.asTriple())
							.closeWriter();
		
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"literal"});
		OnlyValue query = new OnlyValue(dbpedia);
		Collection<CandidateProperty> results = new ContextualizedEvidence(directory, new ConstantSimilarity(), dbpedia).get(request, query.asQuery(request)).asList();
		
		assertThat(results.iterator().next().domains().all(), hasSize(1));
		assertThat(results.iterator().next().domains().all().iterator().next(), equalTo("http://person"));
	}
	
	@Test
	public void shouldSaveOnlyMinimalTypesForObjects() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("entity").asTriple())
																		.closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withObject("http://person").asTriple())
																 .add(new TripleBuilder().withSubject("http://entity").withObject("http://thing").asTriple())
																		 .closeWriter();
		TypeHierarchy hierarchy = new TypeHierarchy(new InputFileTestDouble()
																 .withLine(new TripleBuilder()
																 			.withSubject("http://person").withObject("http://thing").asNTriple()));
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, hierarchy, types, labels, new IndexFields("anyKnowledgeBase"))
							.add(new TripleBuilder()
										.withSubject("http://other_entity")
										.withProperty("http://property")
										.withObject("http://entity")
										.asTriple())
							.closeWriter();
		
		ContextualizedValues request = new ContextualizedValues("any", new String[]{"entity"});
		OnlyValue query = new OnlyValue(dbpedia);
		Collection<CandidateProperty> results = new ContextualizedEvidence(directory, new ConstantSimilarity(), dbpedia).get(request, query.asQuery(request)).asList();
		
		assertThat(results.iterator().next().ranges().all(), hasSize(1));
		assertThat(results.iterator().next().ranges().all().iterator().next(), equalTo("http://person"));
	}
}
