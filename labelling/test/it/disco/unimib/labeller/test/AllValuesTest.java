package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.IndexFields;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.junit.Test;

public class AllValuesTest {

	@Test
	public void shouldParseASingleValue() throws Exception {
		
		Query query = new AllValues().createQuery("Film", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+literal:film"));
	}
	
	@Test
	public void shouldParseMultipleValues() throws Exception {
		Query query = new AllValues().createQuery("Film Noir", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseMultipleValuesWithAStrangeSeparator() throws Exception {
		Query query = new AllValues().createQuery("Film-Noir", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseMultipleValuesWithOR() throws Exception {
		Query query = new AllValues().createQuery("Film OR Noir", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseMultipleValuesWithDoublePoints() throws Exception {
		Query query = new AllValues().createQuery("Film : Noir", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(+literal:film +literal:noir)"));
	}
	
	@Test
	public void shouldParseAlsoQueriesWithORs() throws Exception {
		
		BooleanQuery query = new AllValues().createQuery("portland, OR", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query, is(notNullValue()));
	}
	
	@Test
	public void shouldParseUri() throws Exception {
		
		BooleanQuery query = new AllValues().createQuery("http://aaa.com", "property", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+property:http://aaa.com"));
	}
}