package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.SearchResult;
import it.disco.unimib.labeller.index.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class UnnormalizedPriorTest {

	@Test
	public void thePriorProbabilityOfAPredicateThatAlwaysOccurAreGreaterThanAnotherOneThatOccursOnlyOneTime() throws Exception {
		HashMap<String, List<SearchResult>> distribution = new HashMap<String, List<SearchResult>>();
		ArrayList<SearchResult> occurrenciesForParis = new ArrayList<SearchResult>();
		occurrenciesForParis.add(new SearchResult("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<SearchResult> occurrenciesForRome = new ArrayList<SearchResult>();
		occurrenciesForRome.add(new SearchResult("capital", 1));
		occurrenciesForRome.add(new SearchResult("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		UnnormalizedPrior prior = new UnnormalizedPrior(distribution);
		
		assertThat(prior.of("capital"), is(greaterThan(prior.of("birthPlace"))));
	}
}
