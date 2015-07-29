package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResourceSet;
import it.disco.unimib.labeller.properties.Distribution;

import java.util.HashMap;
import java.util.Set;

import org.junit.Test;

public class DistributionTest {

	@Test
	public void shouldAggregateAllTheSubjects() throws Exception {
		HashMap<String, CandidateResourceSet> results = new HashMap<String, CandidateResourceSet>();
		CandidateResourceSet first = new CandidateResourceSet();
		first.get(new CandidateResource("party")
			 .occurred()
			 .addSubjectTypes("organization")
			 .addSubjectTypes("thing"));
		results.put("first", first);
		
		CandidateResourceSet second = new CandidateResourceSet();
		second.get(new CandidateResource("party")
			  .occurred()
			  .addSubjectTypes("entity"));
		results.put("second", second);
		
		Set<String> subjects = new Distribution(results).subjectsOf("party");
		
		assertThat(subjects, hasSize(3));
	}
}
