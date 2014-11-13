package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.predicates.CandidatePredicates;
import it.disco.unimib.labeller.predicates.Distribution;

import org.junit.Test;

public class CandidatePredicatesTest {

	@Test
	public void shouldMergeResultsForDifferentValues() throws Exception {
		IndexTestDouble index = new IndexTestDouble()
								.resultFor("italy", "country", 10)
								.resultFor("france", "country", 25);
		
		Distribution results = new Distribution(new CandidatePredicates(index).forValues("any", new String[]{"italy", "france"}, new NoContext()));
		
		assertThat(results.scoreOf("country", "france"), equalTo(25d));
	}
}
