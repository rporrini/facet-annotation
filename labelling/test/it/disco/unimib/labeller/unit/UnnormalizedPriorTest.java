package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.PropertyDistribution;
import it.disco.unimib.labeller.properties.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class UnnormalizedPriorTest {

	@Test
	public void thePriorProbabilityOfAPredicateThatAlwaysOccurAreGreaterThanAnotherOneThatOccursOnlyOneTime() throws Exception {
		HashMap<String, CandidateResources> distribution = new HashMap<String, CandidateResources>();
		
		CandidateResources occurrenciesForParis = new CandidateResources();
		occurrenciesForParis.get(new CandidateResource("capital")).sumScore(1);
		distribution.put("paris", occurrenciesForParis);
		
		CandidateResources occurrenciesForRome = new CandidateResources();
		occurrenciesForRome.get(new CandidateResource("capital")).sumScore(1);
		occurrenciesForRome.get(new CandidateResource("birthPlace")).sumScore(1);
		distribution.put("rome", occurrenciesForRome);
		
		UnnormalizedPrior prior = new UnnormalizedPrior(new PropertyDistribution(distribution));
		
		assertThat(prior.of("capital"), is(greaterThan(prior.of("birthPlace"))));
	}
}
