package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnyValue;
import it.disco.unimib.labeller.index.IndexFields;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.junit.Ignore;
import org.junit.Test;

public class AnyValueTest {

	@Test
	public void shouldParseAlsoQueriesWithORs() throws Exception {
		
		BooleanQuery query = new AnyValue().createQuery("portland, OR", "label", new IndexFields("dbpedia").analyzer());
		
		assertThat(query, is(notNullValue()));
	}
	
	@Test
	public void shouldParseQueriesWithStrangeCharacters() throws Exception {
		
		BooleanQuery query = new AnyValue().createQuery("Polar-Express", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(literal:polar literal:express)"));
	}
	
	@Test
	@Ignore
	public void shouldParseQueries() throws Exception {
		
		BooleanQuery query = new AnyValue().createQuery("Polar Express", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(literal:polar literal:express)"));
	}
	
	@Test
	@Ignore
	public void shouldParseMultipleValuesWithDoublePoints() throws Exception {
		Query query = new AnyValue().createQuery("Film : Noir", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(+literal:film literal:noir)"));
	}
}
