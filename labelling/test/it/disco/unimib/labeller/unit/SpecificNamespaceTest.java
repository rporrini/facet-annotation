package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.SpecificNamespace;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import org.apache.lucene.search.Query;
import org.junit.Test;

public class SpecificNamespaceTest {
	@Test
	public void shouldRestrictTheQueryToASpecificNamespace() throws Exception {
		TripleSelectionCriterion query = new SpecificNamespace("thenamespace", new NoContext(new AllValues(new IndexFields("any"))));
		
		Query luceneQuery = query.asQuery(new ContextualizedValues("context", new String[]{"type"}), "literal", "context", "namespace").build();
		
		assertThat(luceneQuery.toString(), containsString("+namespace:\"thenamespace\""));
	}
}
