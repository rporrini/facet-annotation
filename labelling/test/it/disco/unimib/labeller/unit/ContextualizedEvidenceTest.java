package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class ContextualizedEvidenceTest {
	
	@Test
	public void shouldGive0ResultsWhenQueriedAgainstAnEmptyIndex() throws Exception {
		Directory directory = new RAMDirectory();
		
		new EntityValues(directory).closeWriter();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("any", "any", new NoContext(new AllValues())), is(equalTo(0l)));
	}
	
	@Test
	public void shouldGiveOneResultWhenMatchesPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new Evidence(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new IndexFields("dbpedia"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "any", new NoContext(new AllValues())), is(equalTo(1l)));
	}
	
	@Test
	public void shouldGiveOneResultWhenMatchesAPredicateConsideringLabels() throws Exception {
		Directory directory = new RAMDirectory();
		new Evidence(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new IndexFields("dbpedia-with-labels"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "any", new NoContext(new AllValues())), is(equalTo(1l)));
	}
	
	@Test
	public void shouldGiveManyResultWhenMatchesPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new Evidence(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new IndexFields("dbpedia"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "any", new NoContext(new AllValues())), is(equalTo(2l)));
	}
	
	@Test
	public void shouldMatchAlsoPartialContexts() throws Exception {
		Directory directory = new RAMDirectory();
		EntityValues types = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder().withSubject("http://subject")
														.withObject("http://type")
														.asTriple())
								.add(new TripleBuilder().withSubject("http://another_subject")
														.withObject("http://another_type")
														.asTriple())
								.closeWriter();
		EntityValues labels = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder().withSubject("http://type")
														.withLiteral("the type with many terms")
														.asTriple())
								.add(new TripleBuilder().withSubject("http://another_type")
														.withLiteral("another type")
														.asTriple())
								.closeWriter();
		
		new Evidence(directory, 
									types,
									labels,
									new IndexFields("dbpedia"))
								.add(new TripleBuilder()
											.withSubject("http://subject")
											.withPredicate("http://predicate")
									.asTriple())
								.add(new TripleBuilder()
											.withSubject("http://another_subject")
											.withPredicate("http://another-predicate")
									.asTriple())
								.closeWriter();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "one term", new PartialContext(new AllValues())), is(equalTo(1l)));
	}
}
