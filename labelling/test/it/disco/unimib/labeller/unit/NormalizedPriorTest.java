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
		CandidateResource e = new CandidateResource("capital");
		e.sumScore(1);
		occurrenciesForParis.add(e);
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<CandidateResource> occurrenciesForRome = new ArrayList<CandidateResource>();
		CandidateResource e2 = new CandidateResource("capital");
		e2.sumScore(1);
		occurrenciesForRome.add(e2);
		CandidateResource e3 = new CandidateResource("birthPlace");
		e3.sumScore(1);
		occurrenciesForRome.add(e3);
		distribution.put("rome", occurrenciesForRome);
		
		NormalizedPrior prior = new NormalizedPrior(new Distribution(distribution), new UnnormalizedPrior(new Distribution(distribution)));
		
		assertThat(prior.of("capital") + prior.of("birthPlace"), equalTo(1.0));
	}
}
