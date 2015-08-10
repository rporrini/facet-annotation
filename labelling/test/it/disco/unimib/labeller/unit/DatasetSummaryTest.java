package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.properties.DatasetSummary;
import it.disco.unimib.labeller.properties.TypeDistribution;

import org.junit.Ignore;
import org.junit.Test;

public class DatasetSummaryTest {

	@Test
	public void typeOccurrencesForPropertiesShouldBeSummedUp() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
											new InputFileTestDouble()
													.withName("dbpedia.org_ontology_name")
													.withLine("type|1|0|3")
													.withLine("type|1|0|4"));
		
		assertThat(distributions.of("http://dbpedia.org/ontology/name").propertyOccurrenceForType("type"), equalTo(7.0));
	}
	
	@Test
	public void typeOccurrencesShouldNotBeSummedUp() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
											new InputFileTestDouble()
													.withName("dbpedia.org_ontology_name")
													.withLine("type|1|0|0")
													.withLine("type|1|0|0"));
		
		assertThat(distributions.of("http://dbpedia.org/ontology/name").typeOccurrence("type"), equalTo(1.0));
	}
	
	@Test
	public void propertyOccurrencesShouldBeSummedIfDifferent() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
											new InputFileTestDouble()
													.withName("dbpedia.org_ontology_name")
													.withLine("a_type|0|1|0")
													.withLine("another_type|0|2|0"));
		
		assertThat(distributions.of("http://dbpedia.org/ontology/name").propertyOccurrence(), equalTo(3.0));
	}
	
	@Test
	@Ignore
	public void propertyOccurrencesShouldBeSummedIfTheSameTypesOccur() throws Exception {
		
		DatasetSummary distributions = new DatasetSummary(
											new InputFileTestDouble()
													.withName("dbpedia.org_ontology_name")
													.withLine("type|0|1|0")
													.withLine("another_type|0|1|0")
													.withLine("type|0|1|0")
													.withLine("another_type|0|1|0"));
		
		assertThat(distributions.of("http://dbpedia.org/ontology/name").propertyOccurrence(), equalTo(2.0));
	}
	
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
