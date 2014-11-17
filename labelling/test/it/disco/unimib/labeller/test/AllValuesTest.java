package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.IndexFields;

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
}
