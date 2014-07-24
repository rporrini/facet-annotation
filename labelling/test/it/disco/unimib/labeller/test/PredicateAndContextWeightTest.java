package it.disco.unimib.labeller.test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import it.disco.unimib.labeller.index.ContextualizedPredicates;
import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.labelling.PredicateAndContextWeight;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class PredicateAndContextWeightTest {
	
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
		new ContextualizedPredicates(directory, 
										types,
										labels,
										new RankByFrequency(),
										new OptionalContext(),
										new KnowledgeBase("dbpedia"))
												.add(new TripleBuilder().withSubject("a_subject")
																		.withPredicate("predicate")
																		.withLiteral("value").asTriple())
											.closeWriter();
		
		PredicateAndContextWeight predicateAndContextWeight = new PredicateAndContextWeight(new GroupBySearch(directory, new CountPredicates(), new KnowledgeBase("dbpedia")));
		
		double discriminacyMatchingContext = predicateAndContextWeight.discriminacy("predicate", "context", 1);
		double discriminacyNonMatchingContext = predicateAndContextWeight.discriminacy("predicate", "non matching context", 1);
		assertThat(discriminacyMatchingContext, greaterThan(discriminacyNonMatchingContext));
	}

}
