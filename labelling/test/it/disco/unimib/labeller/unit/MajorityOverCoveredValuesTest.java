package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.properties.MajorityOverCoveredValues;

import java.util.List;

import org.junit.Test;

public class MajorityOverCoveredValuesTest {

	@Test
	public void shouldFilterThosePredicatesThatAreBelowACertainThreshold() throws Exception {
		MajorityOverCoveredValues majorityPredicate = new MajorityOverCoveredValues(new IndexTestDouble()
															.resultFor("2012", "predicate", 1)
															.resultFor("2010", "predicate", 1)
															.resultFor("2010", "other predicate", 1), 
															0.6, 
															new OnlyValue(new IndexFields("dbpedia")));
		
		List<CandidateProperty> results = majorityPredicate.annotate(new ContextualizedValues("any", new String[]{"2012", "2010"}));
		
		assertThat(results, hasSize(1));
	}
}
