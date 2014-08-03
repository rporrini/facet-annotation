package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.labelling.LogarithmicContextForPredicate;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class LogarithmicContextForPredicateTest {
	
	@Test
	public void discriminacyShouldBeGreaterIfMatchingContext() throws Exception {
		Directory directory = new RAMDirectory();
		
		TripleIndex types = new EntityValues(directory).add(new TripleBuilder().withSubject("a_subject")
																				.withObject("context")
																				.asTriple())
																.closeWriter();

		TripleIndex labels = new EntityValues(directory).add(new TripleBuilder().withSubject("context")
																				.withLiteral("context")
																				.asTriple())
																.closeWriter();
		new Evidence(directory, 
										types,
										labels,
										new RankByFrequency(),
										new NoContext(),
										new IndexFields("dbpedia"))
												.add(new TripleBuilder().withSubject("a_subject")
																		.withPredicate("predicate")
																		.withLiteral("value").asTriple())
											.closeWriter();
		
		LogarithmicContextForPredicate predicateAndContextWeight = new LogarithmicContextForPredicate(new GroupBySearch(directory, new SimpleOccurrences(), new IndexFields("dbpedia")), new CompleteContext());
		
		double discriminacyMatchingContext = predicateAndContextWeight.of("predicate", "context", 1);
		double discriminacyNonMatchingContext = predicateAndContextWeight.of("predicate", "non matching context", 1);
		assertThat(discriminacyMatchingContext, greaterThan(discriminacyNonMatchingContext));
	}

}
