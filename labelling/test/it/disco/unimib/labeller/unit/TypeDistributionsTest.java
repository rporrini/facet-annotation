package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.properties.TypeDistributions;

import org.junit.Test;

public class TypeDistributionsTest {

	@Test
	public void shouldReturnAnEmptyDistributionWhenNoPropertiesAreProvided() throws Exception {
		
		TypeDistributions distributions = new TypeDistributions(new InputFileTestDouble());
		
		assertThat(distributions.of("any-property").size(), equalTo(0));
	}
	
	@Test
	public void shouldMatchThePropertyName() throws Exception {
		
		TypeDistributions distributions = new TypeDistributions(
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_name")
															.withLine("type|0|1|2")
										);
		
		assertThat(distributions.of("http://dbpedia.org/ontology/name").size(), equalTo(1));
	}
	
	@Test
	public void shouldLoadMoreThanOnePropertyDistributions() throws Exception {
		
		TypeDistributions distributions = new TypeDistributions(
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_name")
															.withLine("type|0|1|2"),
													new InputFileTestDouble()
															.withName("dbpedia.org_ontology_surname")
															.withLine("type|0|1|2")
										);
		
		assertThat(distributions.of("http://dbpedia.org/ontology/surname").size(), equalTo(1));
	}
}
