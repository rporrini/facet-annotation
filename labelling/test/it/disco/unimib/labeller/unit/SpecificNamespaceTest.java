package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.SpecificNamespace;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class SpecificNamespaceTest {
	@Test
	public void shouldRestrictTheQueryToASpecificNamespace() throws Exception {
		TripleSelectionCriterion query = new SpecificNamespace("thenamespace", new NoContext(new AllValues()));
		
		Query luceneQuery = query.asQuery("type", "context", "literal", "context", "namespace", new StandardAnalyzer(Version.LUCENE_45)).build();
		
		assertThat(luceneQuery.toString(), containsString("+namespace:\"thenamespace\""));
	}
}
