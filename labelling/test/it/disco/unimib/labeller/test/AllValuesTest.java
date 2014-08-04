package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.IndexFields;

import org.apache.lucene.search.BooleanQuery;
import org.junit.Test;

public class AllValuesTest {

	@Test
	public void shouldParseAlsoQueriesWithORs() throws Exception {
		
		BooleanQuery query = new AllValues().createQuery("portland, OR", "label", new IndexFields("dbpedia").analyzer());
		
		assertThat(query, is(notNullValue()));
	}
}
