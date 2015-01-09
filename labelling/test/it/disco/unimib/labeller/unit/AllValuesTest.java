package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.Constraint;
import it.disco.unimib.labeller.index.IndexFields;

import org.junit.Test;

public class AllValuesTest {

	@Test
	public void shouldParseASingleValue() throws Exception {
		
		Constraint query = new AllValues(new IndexFields("dbpedia")).createQuery("Film", "literal");
		
		assertThat(query.build().toString(), equalTo("+literal:film"));
	}
}
