package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.properties.Distribution;
import it.disco.unimib.labeller.properties.NormalizedPrior;
import it.disco.unimib.labeller.properties.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class NormalizedPriorTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, CandidateResourceSet> distribution = new HashMap<String, CandidateResourceSet>();
		
		CandidateResourceSet occurrenciesForParis = new CandidateResourceSet();
		occurrenciesForParis.get(new CandidateResource("capital")).sumScore(1);
		distribution.put("paris", occurrenciesForParis);
		
		CandidateResourceSet occurrenciesForRome = new CandidateResourceSet();
		occurrenciesForRome.get(new CandidateResource("capital")).sumScore(1);
		occurrenciesForRome.get(new CandidateResource("birthPlace")).sumScore(1);
		distribution.put("rome", occurrenciesForRome);
		
		NormalizedPrior prior = new NormalizedPrior(new Distribution(distribution), new UnnormalizedPrior(new Distribution(distribution)));
		
		assertThat(prior.of("capital") + prior.of("birthPlace"), equalTo(1.0));
	}
}
