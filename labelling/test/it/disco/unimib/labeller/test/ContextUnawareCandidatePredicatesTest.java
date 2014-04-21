package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ContextUnawareCandidatePredicates;
import it.disco.unimib.labeller.index.SearchResult;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class ContextUnawareCandidatePredicatesTest {

	@Test
	public void shouldReturnEmptyWhenIndexIsEmpty() throws Exception {
		ContextUnawareCandidatePredicates predicates = new ContextUnawareCandidatePredicates(new IndexTestDouble());
		
		assertThat(predicates.forValues("any").get("any"), empty());
	}
	
	@Test
	public void shouldMergeResultsForDifferentValues() throws Exception {
		IndexTestDouble index = new IndexTestDouble()
								.resultFor("italy", "country", 10)
								.resultFor("france", "country", 25);
		
		HashMap<String, List<SearchResult>> results = new ContextUnawareCandidatePredicates(index).forValues("italy", "france");
		
		assertThat(results.get("france").get(0).score(), equalTo(25d));
	}
}
