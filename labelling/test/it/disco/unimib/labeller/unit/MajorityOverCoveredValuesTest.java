package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.predicates.MajorityOverCoveredValues;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MajorityOverCoveredValuesTest {

	@Test
	public void shouldFilterThosePredicatesThatAreBelowACertainThreshold() throws Exception {
		MajorityOverCoveredValues majorityPredicate = new MajorityOverCoveredValues(new IndexTestDouble()
															.resultFor("2012", "predicate", 1)
															.resultFor("2010", "predicate", 1)
															.resultFor("2010", "other predicate", 1), 0.6, new NoContext(new AllValues()));
		
		List<CandidateResource> results = majorityPredicate.typeOf("any", Arrays.asList(new String[]{"2012", "2010"}));
		
		assertThat(results, hasSize(1));
	}
}
