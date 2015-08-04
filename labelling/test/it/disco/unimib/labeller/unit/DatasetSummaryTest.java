package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.properties.TypeDistribution;
import it.disco.unimib.labeller.properties.DatasetSummary;

import org.junit.Test;

public class DatasetSummaryTest {

	@Test
	public void shouldReturnAnEmptyDistributionWhenNoPropertiesAreProvided() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(new InputFileTestDouble());
		
		assertThat(distributions.of("any-property").size(), equalTo(0));
	}
	
	@Test
	public void shouldMatchThePropertyName() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_name")
															.withLine("type|0|1|2")
										);
		
		assertThat(distributions.of("http://dbpedia.org/ontology/name").size(), equalTo(1));
	}
	
	@Test
	public void shouldLoadMoreThanOnePropertyDistributions() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_name")
															.withLine("type|0|1|2"),
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_surname")
															.withLine("type|0|1|2")
										);
		
		assertThat(distributions.of("http://dbpedia.org/ontology/surname").size(), equalTo(1));
	}
	
	@Test
	public void shouldExposeTheRightProperties() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_name")
															.withLine("type|0|1|2")
															.withLine("another_type|3|1|5")
										);
		
		TypeDistribution distribution = distributions.of("http://dbpedia.org/ontology/name");
		
		assertThat(distribution.propertyOccurrence(), equalTo(1.0));
		
		assertThat(distribution.typeOccurrence("type"), equalTo(0.0));
		assertThat(distribution.propertyOccurrenceForType("type"), equalTo(2.0));
		
		assertThat(distribution.typeOccurrence("another_type"), equalTo(3.0));
		assertThat(distribution.propertyOccurrenceForType("another_type"), equalTo(5.0));
	}
}
