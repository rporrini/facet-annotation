package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnyValue;
import it.disco.unimib.labeller.index.IndexFields;

import org.apache.lucene.search.BooleanQuery;
import org.junit.Test;

public class AnyValueTest {

	@Test
	public void shouldParseQueries() throws Exception {
		
		BooleanQuery query = new AnyValue().createQuery("Polar Express", "literal", new IndexFields("dbpedia").analyzer());
		
		assertThat(query.toString(), equalTo("+(literal:polar literal:express)"));
	}
}
