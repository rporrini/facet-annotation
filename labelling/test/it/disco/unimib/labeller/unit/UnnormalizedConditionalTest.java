package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.predicates.Distribution;
import it.disco.unimib.labeller.predicates.NormalizedPrior;
import it.disco.unimib.labeller.predicates.UnnormalizedConditional;
import it.disco.unimib.labeller.predicates.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class UnnormalizedConditionalTest {

	@Test
	public void aPredicateThatThatAppearsMoreFrequentlyThanAnotherHasAGreaterConditionalProbility() {
		HashMap<String, CandidateResourceSet> distribution = new HashMap<String, CandidateResourceSet>();
		CandidateResourceSet results = new CandidateResourceSet();
		results.get(new CandidateResource("capital")).sumScore(25);
		results.get(new CandidateResource("city")).sumScore(10);
		distribution.put("paris", results);
		
		Distribution d = new Distribution(distribution);
		
		UnnormalizedConditional probability = new UnnormalizedConditional(d, new NormalizedPrior(d, new UnnormalizedPrior(d)));
		
		assertThat(probability.of("capital", "paris"), greaterThan(probability.of("city", "paris")));
	}
}
