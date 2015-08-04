package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.PropertyDistribution;
import it.disco.unimib.labeller.properties.MaximumLikelihood;
import it.disco.unimib.labeller.properties.NormalizedConditional;
import it.disco.unimib.labeller.properties.NormalizedPrior;
import it.disco.unimib.labeller.properties.UnnormalizedConditional;
import it.disco.unimib.labeller.properties.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class MaximumLikelihoodTest {

	@Test
	public void theLikelihoodOfAPredicateThatOccurWithManyValuesIsGreaterThanTheLikelihoodOfAPredicateThatOccurWithOnlyAValue() {
		HashMap<String, CandidateResources> distribution = new HashMap<String, CandidateResources>();
		
		CandidateResources occurrenciesForParis = new CandidateResources();
		occurrenciesForParis.get(new CandidateProperty("capital")).sumScore(1);
		distribution.put("paris", occurrenciesForParis);
		
		CandidateResources occurrenciesForRome = new CandidateResources();
		occurrenciesForRome.get(new CandidateProperty("capital")).sumScore(1);
		occurrenciesForRome.get(new CandidateProperty("birthPlace")).sumScore(1);
		distribution.put("rome", occurrenciesForRome);
		
		PropertyDistribution d = new PropertyDistribution(distribution);
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(d);
		NormalizedPrior prior = new NormalizedPrior(d, unnormalizedPrior);
		NormalizedConditional conditional = new NormalizedConditional(d, prior, new UnnormalizedConditional(d, prior));
		
		MaximumLikelihood likelihood = new MaximumLikelihood(d, conditional, prior);
		
		assertThat(likelihood.of("capital"), greaterThan(likelihood.of("birthPlace")));
	}
}
