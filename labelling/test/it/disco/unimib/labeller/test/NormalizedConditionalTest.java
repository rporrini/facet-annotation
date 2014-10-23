package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.predicates.Distribution;
import it.disco.unimib.labeller.predicates.NormalizedConditional;
import it.disco.unimib.labeller.predicates.NormalizedPrior;
import it.disco.unimib.labeller.predicates.UnnormalizedConditional;
import it.disco.unimib.labeller.predicates.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class NormalizedConditionalTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, List<CandidatePredicate>> distribution = new HashMap<String, List<CandidatePredicate>>();
		ArrayList<CandidatePredicate> occurrenciesForParis = new ArrayList<CandidatePredicate>();
		occurrenciesForParis.add(new CandidatePredicate("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<CandidatePredicate> occurrenciesForRome = new ArrayList<CandidatePredicate>();
		occurrenciesForRome.add(new CandidatePredicate("capital", 1));
		occurrenciesForRome.add(new CandidatePredicate("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		Distribution d = new Distribution(distribution);
		NormalizedPrior prior = new NormalizedPrior(d, new UnnormalizedPrior(d));
		UnnormalizedConditional unnormalizedConditional = new UnnormalizedConditional(d, prior);
		
		NormalizedConditional conditional = new NormalizedConditional(d, prior, unnormalizedConditional);
		
		assertThat(conditional.of("capital", "paris") + 
				   conditional.of("birthPlace", "paris") +
				   conditional.of("capital", "rome") + 
				   conditional.of("birthPlace", "rome"), equalTo(1.0));
	}

}
