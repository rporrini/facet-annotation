package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.AnyValue;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.CompleteContext;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.index.TripleIndex;
import it.disco.unimib.labeller.predicates.AnnotationAlgorithm;
import it.disco.unimib.labeller.predicates.Constant;
import it.disco.unimib.labeller.predicates.LogarithmicPredicateSpecificy;
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
		
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new Constant());
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).value(), equalTo("other predicate"));
	}
	
	@Test
	public void shouldCumulateHits() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1);

		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new Constant());
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));

		assertThat(results.get(0).score(), equalTo(1.0986122890014431));
	}

	@Test
	public void shouldNotOrderWithoutConsideringTheWeightOfPredicates() throws Exception {
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new Constant());
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), equalTo(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesInContext() throws Exception {	
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new LogarithmicPredicateSpecificy(index));
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesOnYago() throws Exception {	
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("yago1"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new LogarithmicPredicateSpecificy(index));
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesOnDbpediaWithLabels() throws Exception {	
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia-with-labels"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new LogarithmicPredicateSpecificy(index));
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringPartialContext() throws Exception {	
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new PartialContext(new AnyValue()), new LogarithmicPredicateSpecificy(index));
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldOrderConsideringCompleteContext() throws Exception {	
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new CompleteContext(new AllValues()), new LogarithmicPredicateSpecificy(index));
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldConsiderCoverageOfThePredicatesOverValues() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "lowCoveragePredicate", 2)
				 									 .resultFor("2010", "highCoveragePredicate", 1)
				 									 .resultFor("2012", "highCoveragePredicate", 1);
		
		AnnotationAlgorithm algorithm = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new Constant());
		
		List<CandidatePredicate> results = algorithm.typeOf("context", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).label(), equalTo("highCoveragePredicate"));
	}
	
	@Test
	public void shouldOrderConsideringTheWeightOfPredicatesAndValues() throws Exception {
		Directory directory = buildIndex();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		AnnotationAlgorithm majorityHitWeighted = new WeightedFrequencyCoverageAndSpecificity(index, new NoContext(new AllValues()), new Constant());
		
		List<CandidatePredicate> results = majorityHitWeighted.typeOf("context", Arrays.asList(new String[]{"value", "another_value"}));
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}

	private Directory buildIndex() throws Exception {
		Directory directory = new RAMDirectory();
		TripleIndex types = new EntityValues(directory).add(new TripleBuilder().withSubject("http://a_subject")
																				.withObject("http://context")
																				.asTriple())
														.add(new TripleBuilder().withSubject("http://a_subject_with_partial_context")
																				.withObject("http://partial_context")
																				.asTriple())
																.closeWriter();
		
		TripleIndex labels = new EntityValues(directory).add(new TripleBuilder().withSubject("http://context")
																				.withLiteral("context")
																				.asTriple())
														.add(new TripleBuilder().withSubject("http://partial_context")
																				.withLiteral("partial context")
																				.asTriple())
										.closeWriter();
		new Evidence(directory, 
									types,
									labels,
									new RankByFrequency(),
									new NoContext(new AllValues()),
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
