package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.IndexQuery;

import org.junit.Test;

public class AllValuesTest {

	@Test
	public void shouldParseASingleValue() throws Exception {
		
		IndexQuery query = new AllValues(new IndexFields("dbpedia").analyzer()).createQuery("Film", "literal");
		
		assertThat(query.build().toString(), equalTo("+literal:film"));
	}
}
