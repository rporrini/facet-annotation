package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.labelling.CandidatePredicates;
import it.disco.unimib.labeller.labelling.Predicates;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class CandidatePredicatesTest {

	@Test
	public void shouldReturnEmptyWhenIndexIsEmpty() throws Exception {
		Predicates predicates = new CandidatePredicates(new IndexTestDouble());
		
		assertThat(predicates.forValues("any", new String[]{"any"}, new OptionalContext()).get("any"), empty());
	}
	
	@Test
	public void shouldMergeResultsForDifferentValues() throws Exception {
		IndexTestDouble index = new IndexTestDouble()
								.resultFor("italy", "country", 10)
								.resultFor("france", "country", 25);
		
		HashMap<String, List<CandidatePredicate>> results = new CandidatePredicates(index).forValues("any", new String[]{"italy", "france"}, new OptionalContext());
		
		assertThat(results.get("france").get(0).score(), equalTo(25d));
	}
}
