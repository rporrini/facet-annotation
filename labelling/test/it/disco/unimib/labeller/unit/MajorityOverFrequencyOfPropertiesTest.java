package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.OnlyValue;
import it.disco.unimib.labeller.properties.MajorityOverFrequencyOfProperties;

import java.util.List;

import org.junit.Test;

public class MajorityOverFrequencyOfPropertiesTest{
	
	@Test
	public void shouldOrderOnlyByHit() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1)
													 .resultFor("2010", "other predicate", 10);
		
		
		MajorityOverFrequencyOfProperties majorityHitWeighted = new MajorityOverFrequencyOfProperties(index, new OnlyValue(new IndexFields("dbpedia")));
		
		List<CandidateProperty> results = majorityHitWeighted.annotate(new ContextualizedValues("any", new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).uri(), equalTo("other predicate"));
	}
	
	@Test
	public void shouldCumulateHits() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1);

		MajorityOverFrequencyOfProperties majorityHitWeighted = new MajorityOverFrequencyOfProperties(index, new OnlyValue(new IndexFields("dbpedia")));
		
		List<CandidateProperty> results = majorityHitWeighted.annotate(new ContextualizedValues("any", new String[]{"2012", "2010"}));

		assertThat(results.get(0).score(), equalTo(2.0));
	}
}