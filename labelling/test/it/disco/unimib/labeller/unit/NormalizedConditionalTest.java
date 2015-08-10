package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.PropertyDistribution;
import it.disco.unimib.labeller.properties.NormalizedConditional;
import it.disco.unimib.labeller.properties.NormalizedPrior;
import it.disco.unimib.labeller.properties.UnnormalizedConditional;
import it.disco.unimib.labeller.properties.UnnormalizedPrior;

import java.util.HashMap;

import org.junit.Test;

public class NormalizedConditionalTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, CandidateResources> distribution = new HashMap<String, CandidateResources>();
		
		CandidateResources occurrenciesForParis = new CandidateResources();
		occurrenciesForParis.get(new CandidateProperty("capital")).sumScore(1);
		distribution.put("paris", occurrenciesForParis);
		
		CandidateResources occurrenciesForRome = new CandidateResources();
		occurrenciesForRome.get(new CandidateProperty("capital")).sumScore(1);
		occurrenciesForRome.get(new CandidateProperty("birthPlace")).sumScore(1);
		distribution.put("rome", occurrenciesForRome);
		
		PropertyDistribution d = new PropertyDistribution(distribution);
		NormalizedPrior prior = new NormalizedPrior(d, new UnnormalizedPrior(d));
		UnnormalizedConditional unnormalizedConditional = new UnnormalizedConditional(d, prior);
		
		NormalizedConditional conditional = new NormalizedConditional(d, prior, unnormalizedConditional);
		
		assertThat(conditional.of("capital", "paris") + 
				   conditional.of("birthPlace", "paris") +
				   conditional.of("capital", "rome") + 
				   conditional.of("birthPlace", "rome"), equalTo(1.0));
	}

}
