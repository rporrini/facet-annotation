package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.PropertyDistribution;
import it.disco.unimib.labeller.properties.NormalizedPrior;
import it.disco.unimib.labeller.properties.UnnormalizedConditional;
import it.disco.unimib.labeller.properties.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class UnnormalizedConditionalTest {

	@Test
	public void aPredicateThatThatAppearsMoreFrequentlyThanAnotherHasAGreaterConditionalProbility() {
		HashMap<String, CandidateResources> distribution = new HashMap<String, CandidateResources>();
		CandidateResources results = new CandidateResources();
		results.get(new CandidateProperty("capital")).sumScore(25);
		results.get(new CandidateProperty("city")).sumScore(10);
		distribution.put("paris", results);
		
		PropertyDistribution d = new PropertyDistribution(distribution);
		
		UnnormalizedConditional probability = new UnnormalizedConditional(d, new NormalizedPrior(d, new UnnormalizedPrior(d)));
		
		assertThat(probability.of("capital", "paris"), greaterThan(probability.of("city", "paris")));
	}
}
