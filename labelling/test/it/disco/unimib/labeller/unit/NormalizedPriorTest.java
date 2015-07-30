package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.PropertyDistribution;
import it.disco.unimib.labeller.properties.NormalizedPrior;
import it.disco.unimib.labeller.properties.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class NormalizedPriorTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, CandidateResources> distribution = new HashMap<String, CandidateResources>();
		
		CandidateResources occurrenciesForParis = new CandidateResources();
		occurrenciesForParis.get(new CandidateResource("capital")).sumScore(1);
		distribution.put("paris", occurrenciesForParis);
		
		CandidateResources occurrenciesForRome = new CandidateResources();
		occurrenciesForRome.get(new CandidateResource("capital")).sumScore(1);
		occurrenciesForRome.get(new CandidateResource("birthPlace")).sumScore(1);
		distribution.put("rome", occurrenciesForRome);
		
		NormalizedPrior prior = new NormalizedPrior(new PropertyDistribution(distribution), new UnnormalizedPrior(new PropertyDistribution(distribution)));
		
		assertThat(prior.of("capital") + prior.of("birthPlace"), equalTo(1.0));
	}
}
