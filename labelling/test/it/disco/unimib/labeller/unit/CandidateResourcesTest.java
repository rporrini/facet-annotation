package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResources;

import org.junit.Test;

public class CandidateResourcesTest {

	@Test
	public void shouldReturnAnEmptyResourceWhenAskedForANotExistingResource() throws Exception {
		
		CandidateResources set = new CandidateResources();
		
		CandidateResource resource = set.get(new CandidateResource("predicate"));
		
		assertThat(resource.label(), equalTo("predicate"));
		assertThat(resource.score(), equalTo(0.0));
	}
	
	@Test
	public void shouldGetAPreviuslyAddedResource() throws Exception {
		
		CandidateResources set = new CandidateResources();
		
		set.get(new CandidateResource("predicate")).sumScore(10);
		
		assertThat(set.get(new CandidateResource("predicate")).score(), equalTo(10.0));
	}
	
	@Test
	public void shouldWorkWithPredicates() throws Exception {
		
		CandidateResources set = new CandidateResources();
		
		set.get(new CandidateResource("http://dbpedia.org/ontology/predicate")).sumScore(10);
				
		assertThat(set.get(new CandidateResource("http://dbpedia.org/property/predicate")).score(), equalTo(0.0));
	}
}
