package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.predicates.Distribution;
import it.disco.unimib.labeller.predicates.NormalizedPrior;
import it.disco.unimib.labeller.predicates.UnnormalizedConditional;
import it.disco.unimib.labeller.predicates.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class UnnormalizedConditionalTest {

	@Test
	public void aPredicateThatThatAppearsMoreFrequentlyThanAnotherHasAGreaterConditionalProbility() {
		HashMap<String, List<CandidateResource>> distribution = new HashMap<String, List<CandidateResource>>();
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		CandidateResource e = new CandidateResource("capital");
		e.sumScore(25);
		results.add(e);
		CandidateResource e2 = new CandidateResource("city");
		e2.sumScore(10);
		results.add(e2);
		distribution.put("paris", results);
		
		Distribution d = new Distribution(distribution);
		
		UnnormalizedConditional probability = new UnnormalizedConditional(d, new NormalizedPrior(d, new UnnormalizedPrior(d)));
		
		assertThat(probability.of("capital", "paris"), greaterThan(probability.of("city", "paris")));
	}
}
