package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.predicates.MajorityOverFrequencyOfPredicates;

import java.util.List;

import org.junit.Test;

public class MajorityOverFrequencyOfPredicatesTest{
	
	@Test
	public void shouldOrderOnlyByHit() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1)
													 .resultFor("2010", "other predicate", 10);
		
		
		MajorityOverFrequencyOfPredicates majorityHitWeighted = new MajorityOverFrequencyOfPredicates(index, new NoContext(new AllValues(new IndexFields("dbpedia"))));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf(new ContextualizedValues("any", new String[]{"2012", "2010"}));
		
		assertThat(results.get(0).id(), equalTo("other predicate"));
	}
	
	@Test
	public void shouldCumulateHits() throws Exception {
		IndexTestDouble index = new IndexTestDouble().resultFor("2012", "predicate", 1)
													 .resultFor("2010", "predicate", 1);

		MajorityOverFrequencyOfPredicates majorityHitWeighted = new MajorityOverFrequencyOfPredicates(index, new NoContext(new AllValues(new IndexFields("dbpedia"))));
		
		List<CandidateResource> results = majorityHitWeighted.typeOf(new ContextualizedValues("any", new String[]{"2012", "2010"}));

		assertThat(results.get(0).score(), equalTo(2.0));
	}
}