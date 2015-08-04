package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;
import it.disco.unimib.labeller.properties.DatasetStatistics;

import java.util.HashMap;

import org.junit.Test;

public class DatasetStatisticsTest {

	@Test
	public void shouldComputeTheTotalOccurrenceOfAType() {
		
		CandidateResources firstResources = new CandidateResources();
		firstResources.get(new CandidateProperty("property")).addDomains("type").addRanges("type");
		
		HashMap<String, CandidateResources> results = new HashMap<String, CandidateResources>();
		results.put("any value", firstResources);
		results.put("any other value", firstResources);
		
		DatasetStatistics datasetStatistics = new DatasetStatistics(results);
		
		assertThat(datasetStatistics.domainsOf("property").typeOccurrence("type"), equalTo(4.0));
		assertThat(datasetStatistics.rangesOf("property").typeOccurrence("type"), equalTo(4.0));
	}
}
