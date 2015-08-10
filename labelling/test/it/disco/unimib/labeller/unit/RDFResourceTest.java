package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.index.RDFResource;

import java.util.HashMap;

import org.junit.Test;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Resource;

public class RDFResourceTest {

	@Test
	public void shouldGiveTheLabelOfDBPediaProperties() throws Exception {
		RDFResource result = new RDFResource(new Resource("http://dbpedia.org/property/name"));
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOfDBPediaOntologyProperties() throws Exception {
		RDFResource result = new RDFResource(new Resource("http://dbpedia.org/ontology/name"));
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOnDublinCoreSubject() throws Exception {
		RDFResource result = new RDFResource(new Resource("http://www.w3.org/2004/02/skos/core#subject"));		
		
		assertThat(result.label(), equalTo("subject"));
	}

	@Test
	public void shouldGetTheNamespaceOfDublinCoreTerms() throws Exception {
		RDFResource result = new RDFResource(new Resource("http://www.w3.org/2004/02/skos/core#subject"));
		
		assertThat(result.namespace(), equalTo("http://www.w3.org/2004/02/skos/core#"));
	}
	
	@Test
	public void shouldGetTheNamespaceOfDBPediaTerms() throws Exception {
		RDFResource result = new RDFResource(new Resource("http://dbpedia.org/ontology/name"));
		
		assertThat(result.namespace(), equalTo("http://dbpedia.org/ontology/"));
	}
	
	@Test
	public void shouldParseALiteralValue() throws Exception {
		
		RDFResource resource = new RDFResource(new Literal("12"));
		
		assertThat(resource.label(), equalTo("12"));
	}
	
	@Test
	public void shouldParseTheLiteralDatatype() throws Exception {
		
		RDFResource resource = new RDFResource(new Literal("12", new Resource("integer")));
		
		assertThat(resource.datatype().uri(), equalTo("integer"));
	}
	
	@Test
	public void shouldAttachTheDefaultDatatypeWhenNoDatatypeAssociated() throws Exception {
		RDFResource resource = new RDFResource(new Literal("12"));
		
		assertThat(resource.datatype().uri(), equalTo("http://www.w3.org/1999/02/22-rdf-syntax-ns#Literal"));
	}
	
	@Test
	public void objectsShouldNotBeIdentifiedAsLiteral() throws Exception {
		RDFResource resource = new RDFResource(new Resource("http://aaa"));
		
		assertThat(resource.isLiteral(), equalTo(false));
	}
	
	@Test
	public void literalsShouldNotBeIdentifiedAsLiteral() throws Exception {
		RDFResource resource = new RDFResource(new Literal("12"));
		
		assertThat(resource.isLiteral(), equalTo(true));
	}
	
	@Test
	public void shouldBeIndexedInAnHashMap() throws Exception {
		
		HashMap<RDFResource, String> hashMap = new HashMap<RDFResource, String>();
		
		hashMap.put(new RDFResource(new Resource("http://resource")), "the value");
		
		assertThat(hashMap.get(new RDFResource(new Resource("http://resource"))), equalTo("the value"));
	}
}
