package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.MajorityHit;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MajorityHitTest {

	@Test
	public void shouldOrderByHit() throws Exception {
		MajorityHit majorityHit = new MajorityHit(new CandidatePredicates(new IndexTestDouble()
									.resultFor("2012", "predicate", 1)
									.resultFor("2010", "predicate", 1)
									.resultFor("2010", "other predicate", 10)), 
									new OptionalContext());
		
		List<AnnotationResult> results = majorityHit.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).value(), equalTo("other predicate"));
	}
	
	@Test
	public void shouldCumulateHits() throws Exception {
		MajorityHit majorityHit = new MajorityHit(new CandidatePredicates(new IndexTestDouble()
									.resultFor("2012", "predicate", 1)
									.resultFor("2010", "predicate", 1)), 
									new OptionalContext());

		List<AnnotationResult> results = majorityHit.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));

		assertThat(results.get(0).score(), equalTo(2.0));
	}
}
