package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.FullTextQuery;
import it.disco.unimib.labeller.index.OptionalContext;
import it.disco.unimib.labeller.index.SpecificNamespace;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class SpecificNamespaceTest {
	@Test
	public void shouldRestrictTheQueryToASpecificNamespace() throws Exception {
		FullTextQuery query = new SpecificNamespace("thenamespace", new OptionalContext());
		
		Query luceneQuery = query.createQuery("type", "context", "literal", "context", "namespace", new StandardAnalyzer(Version.LUCENE_45));
		
		assertThat(luceneQuery.toString(), containsString("+namespace:thenamespace"));
	}
}
