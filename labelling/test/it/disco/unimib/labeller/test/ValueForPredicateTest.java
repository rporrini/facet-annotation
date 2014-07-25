package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.ValueForPredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class ValueForPredicateTest {
	
	@Test
	public void discriminacyShouldBeGreaterIfMatchingValue() throws Exception {
		HashMap<String, List<CandidatePredicate>> valueDistribution = new HashMap<String, List<CandidatePredicate>>();
		ArrayList<CandidatePredicate> values = new ArrayList<CandidatePredicate>();
		values.add(new CandidatePredicate("predicate", 1));
		valueDistribution.put("value", values);
		
		ValueForPredicate predicateAndValueWeight = new ValueForPredicate();
		double discriminacyMatchingValue = predicateAndValueWeight.of("predicate", "value", 1, new Distribution(valueDistribution));
		double discriminacyNonMatchingValue = predicateAndValueWeight.of("predicate", "another_value", 1, new Distribution(valueDistribution));
		
		assertThat(discriminacyMatchingValue, greaterThan(discriminacyNonMatchingValue));
	}

	@Test
	public void discriminacyShouldBeZeroIfFrequencyOfPredicateIsZero() throws Exception {
		HashMap<String, List<CandidatePredicate>> valueDistribution = new HashMap<String, List<CandidatePredicate>>();
		ArrayList<CandidatePredicate> values = new ArrayList<CandidatePredicate>();
		values.add(new CandidatePredicate("predicate", 1));
		valueDistribution.put("value", values);
		
		double discriminacy = new ValueForPredicate().of("predicate", "value", 0, new Distribution(valueDistribution));
		
		assertThat(discriminacy, equalTo(0.0));
	}
}
