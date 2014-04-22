package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class UnnormalizedPriorTest {

	@Test
	public void thePriorProbabilityOfAPredicateThatAlwaysOccurAreGreaterThanAnotherOneThatOccursOnlyOneTime() throws Exception {
		HashMap<String, List<AnnotationResult>> distribution = new HashMap<String, List<AnnotationResult>>();
		ArrayList<AnnotationResult> occurrenciesForParis = new ArrayList<AnnotationResult>();
		occurrenciesForParis.add(new AnnotationResult("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<AnnotationResult> occurrenciesForRome = new ArrayList<AnnotationResult>();
		occurrenciesForRome.add(new AnnotationResult("capital", 1));
		occurrenciesForRome.add(new AnnotationResult("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		UnnormalizedPrior prior = new UnnormalizedPrior(new Distribution(distribution));
		
		assertThat(prior.of("capital"), is(greaterThan(prior.of("birthPlace"))));
	}
}
