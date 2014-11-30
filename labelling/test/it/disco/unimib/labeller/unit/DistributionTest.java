package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.predicates.Distribution;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DistributionTest {

	@Test
	public void shouldProvideTheDistributionOfTheSubjectsGivenAPredicateAndAValue() throws Exception {
		HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
		CandidateResourceSet resultsForNational = new CandidateResourceSet();
		resultsForNational.get(new CandidateResource("party")
										.occurred()
										.occurred()
										.addSubjectTypes("organization", "thing")
										.addSubjectTypes("organization"));
		results.put("national", resultsForNational);
		
		Map<String, Double> subjectsOf = new Distribution(results).subjectsOf("party", "national");
		
		assertThat(subjectsOf, hasEntry("organization", 1.0));
		assertThat(subjectsOf, hasEntry("thing", 0.5));
	}
	
	@Test
	public void shouldProvideTheDistributionOfTheObjectsGivenAPredicateAndAValue() throws Exception {
		HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
		CandidateResourceSet resultsForNational = new CandidateResourceSet();
		resultsForNational.get(new CandidateResource("party")
										.occurred()
										.occurred()
										.addObjectTypes("organization", "thing")
										.addObjectTypes("organization"));
		results.put("national", resultsForNational);
		
		Map<String, Double> subjectsOf = new Distribution(results).objectsOf("party", "national");
		
		assertThat(subjectsOf, hasEntry("organization", 1.0));
		assertThat(subjectsOf, hasEntry("thing", 0.5));
	}
}
