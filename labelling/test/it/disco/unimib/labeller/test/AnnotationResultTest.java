package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.AnnotationResult;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class AnnotationResultTest {

	@Test
	public void shouldBeOrderedDescending() {
		AnnotationResult resultWithHigherScore = new AnnotationResult("any", 20);
		AnnotationResult resultWithLowerScore = new AnnotationResult("any", 10);
		
		ArrayList<AnnotationResult> results = new ArrayList<AnnotationResult>();
		results.add(resultWithLowerScore);
		results.add(resultWithHigherScore);
		
		Collections.sort(results);
		
		assertThat(results.get(0).score(), greaterThan(results.get(1).score()));
	}
	
	@Test
	public void shouldGiveTheLabelOfDBPediaProperties() throws Exception {
		AnnotationResult result = new AnnotationResult("http://dbpedia.org/property/name", 0);
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOfDBPediaOntologyProperties() throws Exception {
		AnnotationResult result = new AnnotationResult("http://dbpedia.org/ontology/name", 0);
		
		assertThat(result.label(), equalTo("name"));
	}
	
	@Test
	public void shouldGiveTheLabelOnDublinCoreSubject() throws Exception {
		AnnotationResult result = new AnnotationResult("http://www.w3.org/2004/02/skos/core#subject", 0);		
		
		assertThat(result.label(), equalTo("subject"));
	}
}
