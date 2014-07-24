package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.ContextualizedPredicates;
import it.disco.unimib.labeller.index.CountPredicates;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.KnowledgeBase;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.labelling.MajorityHitWeighted;
import it.disco.unimib.labeller.labelling.PredicateWeight;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class MajorityHitWeightedTest {

	@Test
	public void shouldOrderConsideringOnlyContextDiscriminacy() throws Exception {
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new CountPredicates(), new KnowledgeBase("dbpedia"));
		
		MajorityHitWeighted majorityHitWeighted = new MajorityHitWeighted(index, new PredicateWeight(index, false));
		
		List<AnnotationResult> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderWithoutWeightingForPredicate() throws Exception {
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new CountPredicates(), new KnowledgeBase("dbpedia"));
		
		MajorityHitWeighted majorityHitWeighted = new MajorityHitWeighted(index);
		
		List<AnnotationResult> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), equalTo(results.get(1).score()));
	}

	private Directory buildIndex() throws Exception {
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
								.add(new TripleBuilder().withSubject("a_subject_without_context")
														.withPredicate("predicate_without_context")
														.withLiteral("value").asTriple())
								.closeWriter();
		return directory;
	}
}
