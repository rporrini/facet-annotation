package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.NormalizedPrior;
import it.disco.unimib.labeller.labelling.UnnormalizedConditional;
import it.disco.unimib.labeller.labelling.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class UnnormalizedConditionalTest {

	@Test
	public void aPredicateThatThatAppearsMoreFrequentlyThanAnotherHasAGreaterConditionalProbility() {
		HashMap<String, List<AnnotationResult>> distribution = new HashMap<String, List<AnnotationResult>>();
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		results.add(new AnnotationResult("capital", 25));
		results.add(new AnnotationResult("city", 10));
		distribution.put("paris", results);
		
		Distribution d = new Distribution(distribution);
		
		UnnormalizedConditional probability = new UnnormalizedConditional(d, new NormalizedPrior(d, new UnnormalizedPrior(d)));
		
		assertThat(probability.of("capital", "paris"), greaterThan(probability.of("city", "paris")));
	}
}
