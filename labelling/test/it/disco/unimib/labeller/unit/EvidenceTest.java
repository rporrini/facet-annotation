package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.SimpleOccurrences;

import java.util.List;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class EvidenceTest {
	
	private final IndexFields yago = new IndexFields("yago1");
	private final IndexFields dbpedia = new IndexFields("dbpedia");

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
										.withPredicate("http://hasCapital")
										.withLiteral("http://paris")
										.asTriple())
							.closeWriter();
		
		ContextualizedEvidence search = new ContextualizedEvidence(directory, new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		assertThat(search.get("city", "any", new NoContext(new AllValues())).get(0).value(), equalTo("http://hasCapital"));
	}
	
	@Test
	public void simpleLiteralsShouldBeSearchableInYago() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		new Evidence(directory, 
								new EntityValues(new RAMDirectory()).closeWriter(), 
								new EntityValues(new RAMDirectory()).closeWriter(), 
								yago)
							.add(new TripleBuilder().withPredicate("http://property").withLiteral("the literal").asTriple()).closeWriter();
		
		CandidateResource searchResult = new ContextualizedEvidence(directory, new SimpleOccurrences(), yago)
										.get("literal", "any", new NoContext(new AllValues())).get(0);
		
		assertThat(searchResult.value(), equalTo("property"));
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
										.withPredicate("http://property")
										.withLiteral("literal")
										.asTriple())
							.add(new TripleBuilder()
										.withSubject("http://another_entity")
										.withPredicate("http://another_property")
										.withLiteral("other literal")
										.asTriple())
							.closeWriter();
		
		List<CandidateResource> results = new ContextualizedEvidence(dbpediaDirectory, new SimpleOccurrences(), dbpedia).get("literal", "type", new PartialContext(new AllValues()));
		
		assertThat(results.get(0).value(), equalTo("http://property"));
		
		new Evidence(new RAMDirectory(), types, labels, yago)
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
		
		results = new ContextualizedEvidence(dbpediaDirectory, new SimpleOccurrences(), yago).get("literal", "type", new PartialContext(new AllValues()));
		
		assertThat(results.get(0).value(), equalTo("property"));
	}
	
	@Test
	public void theContextShouldBeStemmedForEnglish() throws Exception {
		EntityValues labels = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://type").withLiteral("plural types").asTriple()).closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory()).add(new TripleBuilder().withSubject("http://entity").withLiteral("http://type").asTriple()).closeWriter();
		
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, types, labels, new IndexFields("anyKnowledgeBase"))
							.add(new TripleBuilder()
										.withSubject("http://entity")
										.withPredicate("http://property")
										.withLiteral("literal")
										.asTriple())
							.closeWriter();
		
		assertThat(new ContextualizedEvidence(directory, new SimpleOccurrences(), dbpedia).get("literals", "type", new PartialContext(new AllValues())), hasSize(1));
	}
}
