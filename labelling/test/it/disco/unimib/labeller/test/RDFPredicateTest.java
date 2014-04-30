package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.RDFPredicate;

import org.junit.Test;

public class RDFPredicateTest {

	@Test
	public void shouldGiveTheLabelOfDBPediaProperties() throws Exception {
		RDFPredicate result = new RDFPredicate("http://dbpedia.org/property/name");
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOfDBPediaOntologyProperties() throws Exception {
		RDFPredicate result = new RDFPredicate("http://dbpedia.org/ontology/name");
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOnDublinCoreSubject() throws Exception {
		RDFPredicate result = new RDFPredicate("http://www.w3.org/2004/02/skos/core#subject");		
		
		assertThat(result.label(), equalTo("subject"));
	}

	@Test
	public void shouldGetTheNamespaceOfDublinCoreTerms() throws Exception {
		RDFPredicate result = new RDFPredicate("http://www.w3.org/2004/02/skos/core#subject");
		
		assertThat(result.namespace(), equalTo("http://www.w3.org/2004/02/skos/core#"));
	}
	
	@Test
	public void shouldGetTheNamespaceOfDBPediaTerms() throws Exception {
		RDFPredicate result = new RDFPredicate("http://dbpedia.org/ontology/name");
		
		assertThat(result.namespace(), equalTo("http://dbpedia.org/ontology/"));
	}
}
