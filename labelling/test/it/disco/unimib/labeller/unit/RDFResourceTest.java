package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.util.HashMap;

import it.disco.unimib.labeller.index.RDFResource;

import org.junit.Test;

public class RDFResourceTest {

	@Test
	public void shouldGiveTheLabelOfDBPediaProperties() throws Exception {
		RDFResource result = new RDFResource("http://dbpedia.org/property/name");
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOfDBPediaOntologyProperties() throws Exception {
		RDFResource result = new RDFResource("http://dbpedia.org/ontology/name");
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOnDublinCoreSubject() throws Exception {
		RDFResource result = new RDFResource("http://www.w3.org/2004/02/skos/core#subject");		
		
		assertThat(result.label(), equalTo("subject"));
	}

	@Test
	public void shouldGetTheNamespaceOfDublinCoreTerms() throws Exception {
		RDFResource result = new RDFResource("http://www.w3.org/2004/02/skos/core#subject");
		
		assertThat(result.namespace(), equalTo("http://www.w3.org/2004/02/skos/core#"));
	}
	
	@Test
	public void shouldGetTheNamespaceOfDBPediaTerms() throws Exception {
		RDFResource result = new RDFResource("http://dbpedia.org/ontology/name");
		
		assertThat(result.namespace(), equalTo("http://dbpedia.org/ontology/"));
	}
	
	@Test
	public void shouldBeIndexedInAnHashMap() throws Exception {
		
		HashMap<RDFResource, String> hashMap = new HashMap<RDFResource, String>();
		
		hashMap.put(new RDFResource("http://resource"), "the value");
		
		assertThat(hashMap.get(new RDFResource("http://resource")), equalTo("the value"));
	}
}
