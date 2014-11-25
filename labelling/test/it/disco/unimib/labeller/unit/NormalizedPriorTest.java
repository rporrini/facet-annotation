package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.predicates.Distribution;
import it.disco.unimib.labeller.predicates.NormalizedPrior;
import it.disco.unimib.labeller.predicates.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class NormalizedPriorTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, List<CandidateResource>> distribution = new HashMap<String, List<CandidateResource>>();
		ArrayList<CandidateResource> occurrenciesForParis = new ArrayList<CandidateResource>();
		occurrenciesForParis.add(new CandidateResource("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<CandidateResource> occurrenciesForRome = new ArrayList<CandidateResource>();
		occurrenciesForRome.add(new CandidateResource("capital", 1));
		occurrenciesForRome.add(new CandidateResource("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		NormalizedPrior prior = new NormalizedPrior(new Distribution(distribution), new UnnormalizedPrior(new Distribution(distribution)));
		
		assertThat(prior.of("capital") + prior.of("birthPlace"), equalTo(1.0));
	}
}
