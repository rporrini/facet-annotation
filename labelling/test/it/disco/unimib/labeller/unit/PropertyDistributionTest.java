package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.PropertyDistribution;
import it.disco.unimib.labeller.properties.TypeDistribution;

import java.util.HashMap;

import org.junit.Test;

public class PropertyDistributionTest {

	@Test
	public void shouldAggregateAllTheSubjects() throws Exception {
		HashMap<String, CandidateResources> results = new HashMap<String, CandidateResources>();
		CandidateResources first = new CandidateResources();
		first.get(new CandidateProperty("party")
			 .occurred()
			 .addDomains("organization")
			 .addDomains("thing"));
		results.put("first", first);
		
		CandidateResources second = new CandidateResources();
		second.get(new CandidateProperty("party")
			  .occurred()
			  .addDomains("entity"));
		results.put("second", second);
		
		TypeDistribution subjects = new PropertyDistribution(results).asStatistics().domainsOf("party");
		
		assertThat(subjects.size(), equalTo(3));
	}
}
