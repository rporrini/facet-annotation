package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;

import org.junit.Test;

public class CandidateResourceSetTest {

	@Test
	public void shouldReturnAnEmptyResourceWhenAskedForANotExistingResource() throws Exception {
		
		CandidateResourceSet set = new CandidateResourceSet();
		
		CandidateResource resource = set.get(new CandidateResource("predicate"));
		
		assertThat(resource.label(), equalTo("predicate"));
		assertThat(resource.score(), equalTo(0.0));
	}
	
	@Test
	public void shouldGetAPreviuslyAddedResource() throws Exception {
		
		CandidateResourceSet set = new CandidateResourceSet();
		
		set.get(new CandidateResource("predicate")).sumScore(10);
		
		assertThat(set.get(new CandidateResource("predicate")).score(), equalTo(10.0));
	}
	
	@Test
	public void shouldWorkWithPredicates() throws Exception {
		
		CandidateResourceSet set = new CandidateResourceSet();
		
		set.get(new CandidateResource("http://dbpedia.org/ontology/predicate")).sumScore(10);
				
		assertThat(set.get(new CandidateResource("http://dbpedia.org/property/predicate")).score(), equalTo(0.0));
	}
}
