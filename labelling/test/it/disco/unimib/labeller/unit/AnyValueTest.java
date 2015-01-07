package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnyValue;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.IndexQuery;

import org.junit.Test;

public class AnyValueTest {

	@Test
	public void shouldParseQueries() throws Exception {
		
		IndexQuery query = new AnyValue(new IndexFields("dbpedia").analyzer()).createQuery("Polar Express", "literal");
		
		assertThat(query.build().toString(), equalTo("+(literal:polar literal:express)"));
	}
}
