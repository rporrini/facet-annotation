package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.labelling.Distribution;
import it.disco.unimib.labeller.labelling.NormalizedConditional;
import it.disco.unimib.labeller.labelling.NormalizedPrior;
import it.disco.unimib.labeller.labelling.UnnormalizedConditional;
import it.disco.unimib.labeller.labelling.UnnormalizedPrior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class NormalizedConditionalTest {

	@Test
	public void theProbabilitiesOfThePredicateShouldSumToOne() {
		HashMap<String, List<AnnotationResult>> distribution = new HashMap<String, List<AnnotationResult>>();
		ArrayList<AnnotationResult> occurrenciesForParis = new ArrayList<AnnotationResult>();
		occurrenciesForParis.add(new AnnotationResult("capital", 1));
		distribution.put("paris", occurrenciesForParis);
		
		ArrayList<AnnotationResult> occurrenciesForRome = new ArrayList<AnnotationResult>();
		occurrenciesForRome.add(new AnnotationResult("capital", 1));
		occurrenciesForRome.add(new AnnotationResult("birthPlace", 1));
		distribution.put("rome", occurrenciesForRome);
		
		Distribution d = new Distribution(distribution);
		NormalizedPrior prior = new NormalizedPrior(d, new UnnormalizedPrior(d));
		UnnormalizedConditional unnormalizedConditional = new UnnormalizedConditional(d, prior);
		
		NormalizedConditional conditional = new NormalizedConditional(d, prior, unnormalizedConditional);
		
		assertThat(conditional.of("capital", "paris") + 
				   conditional.of("birthPlace", "paris") +
				   conditional.of("capital", "rome") + 
				   conditional.of("birthPlace", "rome"), equalTo(1.0));
	}

}
