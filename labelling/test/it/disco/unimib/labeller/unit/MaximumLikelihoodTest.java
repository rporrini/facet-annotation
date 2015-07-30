package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;
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
		HashMap<String, CandidateResourceSet> distribution = new HashMap<String, CandidateResourceSet>();
		
		CandidateResourceSet occurrenciesForParis = new CandidateResourceSet();
		occurrenciesForParis.get(new CandidateResource("capital")).sumScore(1);
		distribution.put("paris", occurrenciesForParis);
		
		CandidateResourceSet occurrenciesForRome = new CandidateResourceSet();
		occurrenciesForRome.get(new CandidateResource("capital")).sumScore(1);
		occurrenciesForRome.get(new CandidateResource("birthPlace")).sumScore(1);
		distribution.put("rome", occurrenciesForRome);
		
		PropertyDistribution d = new PropertyDistribution(distribution);
		UnnormalizedPrior unnormalizedPrior = new UnnormalizedPrior(d);
		NormalizedPrior prior = new NormalizedPrior(d, unnormalizedPrior);
		NormalizedConditional conditional = new NormalizedConditional(d, prior, new UnnormalizedConditional(d, prior));
		
		MaximumLikelihood likelihood = new MaximumLikelihood(d, conditional, prior);
		
		assertThat(likelihood.of("capital"), greaterThan(likelihood.of("birthPlace")));
	}
}
