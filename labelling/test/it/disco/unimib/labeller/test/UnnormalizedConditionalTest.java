package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.SearchResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.UnnormalizedConditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class UnnormalizedConditionalTest {

	@Test
	public void aPredicateThatThatAppearsMoreFrequentlyThanAnotherHasAGreaterConditionalProbility() {
		HashMap<String, List<SearchResult>> distribution = new HashMap<String, List<SearchResult>>();
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		results.add(new SearchResult("capital", 25));
		results.add(new SearchResult("city", 10));
		distribution.put("paris", results);
		
		UnnormalizedConditional probability = new UnnormalizedConditional(new Distribution(distribution));
		
		assertThat(probability.of("capital", "paris"), greaterThan(probability.of("city", "paris")));
	}
}
