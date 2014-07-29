package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.IndexFields;

import org.junit.Test;

public class IndexFieldsTest {

	@Test
	public void ifKbIsDbpediaShouldConsiderProperties() throws Exception {
		String property = new IndexFields("dbpedia").predicateField();
		
		assertThat(property, equalTo("property"));
	}
	
	@Test
	public void ifKbIsDbpediaWithLabelsShouldConsiderLabels() throws Exception {
		String property = new IndexFields("dbpedia-with-labels").predicateField();
		
		assertThat(property, equalTo("label"));
	}
	
	@Test
	public void ifKbIsYagoShouldConsiderLabels() throws Exception {
		String property = new IndexFields("yago1").predicateField();
		
		assertThat(property, equalTo("label"));
	}
}
