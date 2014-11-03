package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.PartialContext;
import it.disco.unimib.labeller.index.RankByFrequency;
import it.disco.unimib.labeller.index.SimpleOccurrences;
import it.disco.unimib.labeller.index.TripleIndex;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class GroupBySearchTest {
	
	@Test
	public void shouldGive0ResultsWhenQueriedAgainstAnEmptyIndex() throws Exception {
		Directory directory = new RAMDirectory();
		
		new EntityValues(directory).closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("any", "any", new NoContext()), is(equalTo(0l)));
	}
	
	@Test
	public void shouldGiveOneResultWhenMatchesPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new Evidence(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new RankByFrequency(),
									new NoContext(),
									new IndexFields("dbpedia"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "any", new NoContext()), is(equalTo(1l)));
	}
	
	@Test
	public void shouldGiveOneResultWhenMatchesAPredicateConsideringLabels() throws Exception {
		Directory directory = new RAMDirectory();
		new Evidence(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new RankByFrequency(),
									new NoContext(),
									new IndexFields("dbpedia-with-labels"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "any", new NoContext()), is(equalTo(1l)));
	}
	
	@Test
	public void shouldGiveManyResultWhenMatchesPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new Evidence(directory, 
									new EntityValues(new RAMDirectory()).closeWriter(),
									new EntityValues(new RAMDirectory()).closeWriter(),
									new RankByFrequency(),
									new NoContext(),
									new IndexFields("dbpedia"))
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.add(new TripleBuilder().withPredicate("http://predicate").asTriple())
								.closeWriter();
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "any", new NoContext()), is(equalTo(2l)));
	}
	
	@Test
	public void shouldMatchAlsoPartialContexts() throws Exception {
		Directory directory = new RAMDirectory();
		TripleIndex types = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder().withSubject("http://subject")
														.withObject("http://type")
														.asTriple())
								.add(new TripleBuilder().withSubject("http://another_subject")
														.withObject("http://another_type")
														.asTriple())
								.closeWriter();
		TripleIndex labels = new EntityValues(new RAMDirectory())
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
									new RankByFrequency(),
									new NoContext(),
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
		
		GroupBySearch index = new GroupBySearch(directory , new SimpleOccurrences(), new IndexFields("dbpedia"));
		
		assertThat(index.countPredicatesInContext("http://predicate", "one term", new PartialContext()), is(equalTo(1l)));
	}
	
	@Test
	public void shouldGiveZeroResultsWhenCountingANotExistingCombinationOfValueAndPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		new EntityValues(directory).closeWriter();
		
		GroupBySearch search = new GroupBySearch(directory, null, new IndexFields("dbpedia"));
		
		assertThat(search.countValuesForPredicates("any value", "any_predicate"), is(equalTo(0l)));
	}
	
	@Test
	public void shouldCountCoOccurrencesOfAValueForAPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		
		new Evidence(directory, 
						new EntityValues(new RAMDirectory()).closeWriter(), 
						new EntityValues(new RAMDirectory()).closeWriter(),
						null,
						null,
						new IndexFields("dbpedia"))
					.add(new TripleBuilder().withPredicate("the_predicate").withLiteral("the value").asTriple())
					.closeWriter();
		
		GroupBySearch search = new GroupBySearch(directory, null, new IndexFields("dbpedia"));
		
		assertThat(search.countValuesForPredicates("the value", "the_predicate"), is(1l));
	}
	
	@Test
	public void shouldCountCoOccurrencesOfAValueForAPredicateWhenKBIsYago() throws Exception {
		Directory directory = new RAMDirectory();
		
		IndexFields fields = new IndexFields("yago1");
		new Evidence(directory, 
						new EntityValues(new RAMDirectory()).closeWriter(), 
						new EntityValues(new RAMDirectory()).closeWriter(),
						null,
						null,
						fields)
					.add(new TripleBuilder().withPredicate("http://yago1#the_predicate").withLiteral("the value").asTriple())
					.closeWriter();
		
		GroupBySearch search = new GroupBySearch(directory, null, fields);
		
		assertThat(search.countValuesForPredicates("the value", "the_predicate"), is(1l));
	}
	
	@Test
	public void shouldCountCoOccurrencesOfAValueForAPredicateWhenKBIsDBPediaWithLabels() throws Exception {
		Directory directory = new RAMDirectory();
		
		IndexFields fields = new IndexFields("dbpedia-with-labels");
		new Evidence(directory, 
						new EntityValues(new RAMDirectory()).closeWriter(), 
						new EntityValues(new RAMDirectory()).closeWriter(),
						null,
						null,
						fields)
					.add(new TripleBuilder().withPredicate("http://dbpedia.org/property/the_predicate").withLiteral("the value").asTriple())
					.closeWriter();
		
		GroupBySearch search = new GroupBySearch(directory, null, fields);
		
		assertThat(search.countValuesForPredicates("the value", "the_predicate"), is(1l));
	}
	
	@Test
	public void shouldCountManyCoOccurrencesOfAValueForAPredicate() throws Exception {
		Directory directory = new RAMDirectory();
		
		new Evidence(directory, 
						new EntityValues(new RAMDirectory()).closeWriter(), 
						new EntityValues(new RAMDirectory()).closeWriter(),
						null,
						null,
						new IndexFields("dbpedia"))
					.add(new TripleBuilder().withPredicate("the_predicate").withLiteral("first value").asTriple())
					.add(new TripleBuilder().withPredicate("the_predicate").withLiteral("first value").asTriple())
					.add(new TripleBuilder().withPredicate("the_predicate").withLiteral("second value").asTriple())
					.add(new TripleBuilder().withPredicate("another_predicate").withLiteral("first value").asTriple())
					.closeWriter();
		
		GroupBySearch search = new GroupBySearch(directory, null, new IndexFields("dbpedia"));
		
		assertThat(search.countValuesForPredicates("first value", "the_predicate"), is(2l));
	}
}
