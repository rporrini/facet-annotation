package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.MaximumLikelihood;
import it.disco.unimib.labeller.labelling.NormalizedConditional;
import it.disco.unimib.labeller.labelling.NormalizedPrior;
import it.disco.unimib.labeller.labelling.UnnormalizedConditional;
import it.disco.unimib.labeller.labelling.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class MaximumLikelihoodTest {

	@Test
	public void theLikelihoodOfAPredicateThatOccurWithManyValuesIsGreaterThanTheLikelihoodOfAPredicateThatOccurWithOnlyAValue() {
		HashMap<String, List<CandidatePredicate>> distribution = new HashMap<String, List<CandidatePredicate>>();
		ArrayList<CandidatePredicate> occurrenciesForParis = new ArrayList<CandidatePredicate>();
		occurrenciesForParis.add(new CandidatePredicate("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<CandidatePredicate> occurrenciesForRome = new ArrayList<CandidatePredicate>();
		occurrenciesForRome.add(new CandidatePredicate("capital", 1));
		occurrenciesForRome.add(new CandidatePredicate("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		Distribution d = new Distribution(distribution);
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(d);
		NormalizedPrior prior = new NormalizedPrior(d, unnormalizedPrior);
		NormalizedConditional conditional = new NormalizedConditional(d, prior, new UnnormalizedConditional(d, prior));
		
		MaximumLikelihood likelihood = new MaximumLikelihood(d, conditional, prior);
		
		assertThat(likelihood.of("capital"), greaterThan(likelihood.of("birthPlace")));
	}
}
