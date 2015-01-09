package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
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
		
		Distribution results = new CandidatePredicates(index)
									.forValues(new ContextualizedValues("any", new String[]{"italy", "france"}), 
												new NoContext(new AllValues(new IndexFields("dbpedia"))));
		
		assertThat(results.scoreOf("country", "france"), equalTo(25d));
	}
}
