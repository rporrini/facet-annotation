package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
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
		HashMap<String, List<CandidatePredicate>> distribution = new HashMap<String, List<CandidatePredicate>>();
		ArrayList<CandidatePredicate> occurrenciesForParis = new ArrayList<CandidatePredicate>();
		occurrenciesForParis.add(new CandidatePredicate("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<CandidatePredicate> occurrenciesForRome = new ArrayList<CandidatePredicate>();
		occurrenciesForRome.add(new CandidatePredicate("capital", 1));
		occurrenciesForRome.add(new CandidatePredicate("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		NormalizedPrior prior = new NormalizedPrior(new Distribution(distribution), new UnnormalizedPrior(new Distribution(distribution)));
		
		assertThat(prior.of("capital") + prior.of("birthPlace"), equalTo(1.0));
	}
}
