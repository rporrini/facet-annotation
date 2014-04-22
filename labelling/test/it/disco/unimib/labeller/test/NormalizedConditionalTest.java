package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.SearchResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.NormalizedConditional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class NormalizedConditionalTest {

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
		
		NormalizedConditional conditional = new NormalizedConditional(new Distribution(distribution));
		
		assertThat(conditional.of("capital", "paris") + 
				   conditional.of("birthPlace", "paris") +
				   conditional.of("capital", "rome") + 
				   conditional.of("birthPlace", "rome"), equalTo(1.0));
	}

}
