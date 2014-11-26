package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.ContextualizedOccurrences;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.predicates.LogarithmicPredicateSpecificy;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class LogarithmicContextForPredicateTest {
	
	@Test
	public void discriminacyShouldBeGreaterIfMatchingContext() throws Exception {
		Directory directory = new RAMDirectory();
		
		EntityValues types = new EntityValues(directory).add(new TripleBuilder().withSubject("a_subject")
																				.withObject("context")
																				.asTriple())
																.closeWriter();

		EntityValues labels = new EntityValues(directory).add(new TripleBuilder().withSubject("context")
																				.withLiteral("context")
																				.asTriple())
																.closeWriter();
		new Evidence(directory, 
										types,
										labels,
										new IndexFields("dbpedia"))
												.add(new TripleBuilder().withSubject("a_subject")
																		.withPredicate("predicate")
																		.withLiteral("value").asTriple())
											.closeWriter();
		
		LogarithmicPredicateSpecificy predicateAndContextWeight = new LogarithmicPredicateSpecificy(new ContextualizedEvidence(directory, new ContextualizedOccurrences(new ConstantSimilarity()), new IndexFields("dbpedia")));
		
		double discriminacyMatchingContext = predicateAndContextWeight.of("predicate", "context", 1);
		double discriminacyNonMatchingContext = predicateAndContextWeight.of("predicate", "non matching", 1);
		assertThat(discriminacyMatchingContext, greaterThan(discriminacyNonMatchingContext));
	}

}
