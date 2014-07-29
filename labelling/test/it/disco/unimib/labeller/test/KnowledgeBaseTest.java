package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.index.IndexFields;
import it.disco.unimib.labeller.index.KnowledgeBase;

import org.junit.Test;

public class KnowledgeBaseTest {

	@Test
	public void ifKbIsDbpediaShouldConsiderProperties() throws Exception {
		String property = new KnowledgeBase("dbpedia", new IndexFields()).predicateField();
		
		assertThat(property, equalTo("property"));
	}
	
	@Test
	public void ifKbIsDbpediaWithLabelsShouldConsiderLabels() throws Exception {
		String property = new KnowledgeBase("dbpedia-with-labels", new IndexFields()).predicateField();
		
		assertThat(property, equalTo("label"));
	}
	
	@Test
	public void ifKbIsYagoShouldConsiderLabels() throws Exception {
		String property = new KnowledgeBase("yago1", new IndexFields()).predicateField();
		
		assertThat(property, equalTo("label"));
	}
}
