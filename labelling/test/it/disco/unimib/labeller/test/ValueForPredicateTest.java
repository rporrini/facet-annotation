package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.Evidence;
import it.disco.unimib.labeller.index.GroupBySearch;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.labelling.ValueForPredicate;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class ValueForPredicateTest {
	
	@Test
	public void discriminacyShouldBeGreaterIfMatchingValue() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, new EntityValues(new RAMDirectory()).closeWriter(),
												new EntityValues(new RAMDirectory()).closeWriter(),
												null,
												null,
												new IndexFields("dbpedia")).add(new TripleBuilder().withPredicate("predicate")
																								   .withLiteral("value")
																								   .asTriple())
																								   .closeWriter();
		
		ValueForPredicate predicateAndValueWeight = new ValueForPredicate(new GroupBySearch(directory, null, new IndexFields("dbpedia")));
		
		double discriminacyMatchingValue = predicateAndValueWeight.of("predicate", "value", 1);
		double discriminacyNonMatchingValue = predicateAndValueWeight.of("predicate", "another_value", 1);
		
		assertThat(discriminacyMatchingValue, greaterThan(discriminacyNonMatchingValue));
	}

	@Test
	public void discriminacyShouldBeZeroIfFrequencyOfPredicateIsZero() throws Exception {
		RAMDirectory directory = new RAMDirectory();
		
		new Evidence(directory, new EntityValues(new RAMDirectory()).closeWriter(),
												new EntityValues(new RAMDirectory()).closeWriter(),
												null,
												null,
												new IndexFields("dbpedia")).add(new TripleBuilder().withPredicate("predicate")
																								   .withLiteral("value")
																								   .asTriple())
																								   .closeWriter();
		
		double discriminacy = new ValueForPredicate(new GroupBySearch(directory, null, new IndexFields("dbpedia"))).of("predicate", "value", 0);
		
		assertThat(discriminacy, equalTo(0.0));
	}
}
