package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.predicates.Distribution;
import it.disco.unimib.labeller.predicates.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class UnnormalizedPriorTest {

	@Test
	public void thePriorProbabilityOfAPredicateThatAlwaysOccurAreGreaterThanAnotherOneThatOccursOnlyOneTime() throws Exception {
		HashMap<String, List<CandidateResource>> distribution = new HashMap<String, List<CandidateResource>>();
		ArrayList<CandidateResource> occurrenciesForParis = new ArrayList<CandidateResource>();
		occurrenciesForParis.add(new CandidateResource("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<CandidateResource> occurrenciesForRome = new ArrayList<CandidateResource>();
		occurrenciesForRome.add(new CandidateResource("capital", 1));
		occurrenciesForRome.add(new CandidateResource("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		UnnormalizedPrior prior = new UnnormalizedPrior(new Distribution(distribution));
		
		assertThat(prior.of("capital"), is(greaterThan(prior.of("birthPlace"))));
	}
}
