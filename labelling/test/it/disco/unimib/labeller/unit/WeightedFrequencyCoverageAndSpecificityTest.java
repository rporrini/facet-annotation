package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.AnyValue;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.ConstantSimilarity;
import it.disco.unimib.labeller.index.ContextualizedEvidence;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.TypeConsistency;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.Constant;
import it.disco.unimib.labeller.predicates.PredicateContextSpecificity;
import it.disco.unimib.labeller.predicates.WeightedFrequencyCoverageAndSpecificity;

import java.util.Arrays;
import java.util.List;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class WeightedFrequencyCoverageAndSpecificityTest {
	
	@Test
	public void shouldOrderOnlyByHit() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1)
													 .resultFor("2010", "other predicate", 10);
		
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new Constant());
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).id(), equalTo("other predicate"));
	}

	@Test
	public void shouldCumulateHits() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1);

		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new Constant());
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));

		assertThat(results.get(0).score(), equalTo(0.5142717790222688));
	}

	@Test
	public void shouldNotOrderWithoutConsideringTheWeightOfPredicates() throws Exception {
		Directory directory = buildIndex();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new Constant());
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), equalTo(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesInContext() throws Exception {	
		Directory directory = buildIndex();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new PredicateContextSpecificity(index, new IndexFields("dbpedia")));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesOnYago() throws Exception {	
		Directory directory = buildIndex();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("yago1"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new PredicateContextSpecificity(index, new IndexFields("dbpedia")));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesOnDbpediaWithLabels() throws Exception {	
		Directory directory = buildIndex();
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), new IndexFields("dbpedia-with-labels"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new PredicateContextSpecificity(index, new IndexFields("dbpedia")));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringPartialContext() throws Exception {	
		Directory directory = buildIndex();
		
		IndexFields fields = new IndexFields("dbpedia");
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), fields);
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new PartialContext(new AnyValue(fields.analyzer())), new PredicateContextSpecificity(index, new IndexFields("dbpedia")));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringCompleteContext() throws Exception {	
		Directory directory = buildIndex();
		IndexFields fields = new IndexFields("dbpedia");
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), fields);
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new CompleteContext(new AllValues(fields.analyzer())), new PredicateContextSpecificity(index, new IndexFields("dbpedia")));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldConsiderCoverageOfThePredicatesOverValues() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "lowCoveragePredicate", 2)
				 									 .resultFor("2010", "highCoveragePredicate", 1)
				 									 .resultFor("2012", "highCoveragePredicate", 1);
		
		AnnotationAlgorithm algorithm = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(new IndexFields("dbpedia").analyzer())), new Constant());
		
		List<CandidateResource> results = algorithm.typeOf("context", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).label(), equalTo("highCoveragePredicate"));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesAndValues() throws Exception {
		Directory directory = buildIndex();
		IndexFields fields = new IndexFields("dbpedia");
		
		ContextualizedEvidence index = new ContextualizedEvidence(directory , new ConstantSimilarity(), fields);
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(emptyTypes(), index, new NoContext(new AllValues(fields.analyzer())), new Constant());
		
		List<CandidateResource> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value", "another_value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	private TypeConsistency emptyTypes() throws Exception {
		return new ScaledDepths(new InputFileTestDouble());
	}

	private Directory buildIndex() throws Exception {
		Directory directory = new RAMDirectory();
		EntityValues types = new EntityValues(directory).add(new TripleBuilder().withSubject("http://a_subject")
																				.withObject("http://context")
																				.asTriple())
														.add(new TripleBuilder().withSubject("http://a_subject_with_partial_context")
																				.withObject("http://partial_context")
																				.asTriple())
																.closeWriter();
		
		EntityValues labels = new EntityValues(directory).add(new TripleBuilder().withSubject("http://context")
																				.withLiteral("context")
																				.asTriple())
														.add(new TripleBuilder().withSubject("http://partial_context")
																				.withLiteral("partial context")
																				.asTriple())
										.closeWriter();
		new Evidence(directory, 
									types,
									labels,
									new IndexFields("dbpedia"))
										.add(new TripleBuilder().withSubject("http://a_subject")
																.withPredicate("http://predicate")
																.withLiteral("value").asTriple())
										.add(new TripleBuilder().withSubject("http://a_subject_without_context")
																.withPredicate("http://predicate")
																.withLiteral("another_value").asTriple())
										.add(new TripleBuilder().withSubject("http://a_subject_with_partial_context")
																.withPredicate("http://predicate_with_partial_context")
																.withLiteral("value").asTriple())
										.closeWriter();
		return directory;
	}
}
