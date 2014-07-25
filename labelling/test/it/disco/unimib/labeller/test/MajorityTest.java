package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.Majority;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MajorityTest {

	@Test
	public void shouldFilterThosePredicatesThatAreBelowACertainThreshold() throws Exception {
		Majority majorityPredicate = new Majority(new CandidatePredicates(new IndexTestDouble()
															.resultFor("2012", "predicate", 1)
															.resultFor("2010", "predicate", 1)
															.resultFor("2010", "other predicate", 1)), 0.6, new OptionalContext());
		
		List<CandidatePredicate> results = majorityPredicate.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results, hasSize(1));
	}
}
