package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.MaximumLikelihood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class MaximumLikelihoodTest {

	@Test
	public void theLikelihoodOfAPredicateThatOccurWithManyValuesIsGreaterThanTheLikelihoodOfAPredicateThatOccurWithOnlyAValue() {
		HashMap<String, List<AnnotationResult>> distribution = new HashMap<String, List<AnnotationResult>>();
		ArrayList<AnnotationResult> occurrenciesForParis = new ArrayList<AnnotationResult>();
		occurrenciesForParis.add(new AnnotationResult("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<AnnotationResult> occurrenciesForRome = new ArrayList<AnnotationResult>();
		occurrenciesForRome.add(new AnnotationResult("capital", 1));
		occurrenciesForRome.add(new AnnotationResult("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		MaximumLikelihood likelihood = new MaximumLikelihood(new Distribution(distribution));
		
		assertThat(likelihood.of("capital"), greaterThan(likelihood.of("birthPlace")));
	}
}
