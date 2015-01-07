package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.IndexQuery;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class IndexQueryTest {

	@Test
	public void onDefaultShouldBuildAnEmptyQuery() {
		
		IndexQuery builder = new IndexQuery();
		
		assertThat(builder.build(), notNullValue());
	}
	
	@Test
	public void shouldMatchAnExactTerm() throws Exception {
		
		IndexQuery builder = new IndexQuery().matchExactly("http://term", "field");
		
		assertThat(builder.build().toString(), equalTo("+field:\"http://term\""));
	}
	
	@Test
	public void shouldMatchAllTheWordsOfATerm() throws Exception {
		
		IndexQuery query = new IndexQuery(new EnglishAnalyzer(Version.LUCENE_45)).matchAll("c b", "field");
		
		assertThat(query.build().toString(), equalTo("+(+field:c +field:b)"));
	}
}
