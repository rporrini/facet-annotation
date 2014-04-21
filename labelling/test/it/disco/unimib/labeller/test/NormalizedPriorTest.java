package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.SearchResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.NormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class NormalizedPriorTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, List<SearchResult>> distribution = new HashMap<String, List<SearchResult>>();
		ArrayList<SearchResult> occurrenciesForParis = new ArrayList<SearchResult>();
		occurrenciesForParis.add(new SearchResult("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<SearchResult> occurrenciesForRome = new ArrayList<SearchResult>();
		occurrenciesForRome.add(new SearchResult("capital", 1));
		occurrenciesForRome.add(new SearchResult("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		NormalizedPrior prior = new NormalizedPrior(new Distribution(distribution));
		
		assertThat(prior.of("capital") + prior.of("birthPlace"), equalTo(1.0));
	}
}
