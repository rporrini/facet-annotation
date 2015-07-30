package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.properties.TypeDistribution;

import org.junit.Test;

public class TypeDistributionTest {

	@Test
	public void shouldExposeTheOverallFrequencyOfType() {
		TypeDistribution distribution = new TypeDistribution();
		
		distribution.trackTypeOccurrence("type", "1");
		
		assertThat(distribution.typeOccurrence("type"), equalTo(1.0));
	}
	
	@Test
	public void theOverallFrequencyOfANotExistingTypeIsShouldBeMinimal() {
		TypeDistribution distribution = new TypeDistribution();
		
		assertThat(distribution.typeOccurrence("type"), equalTo(0.0));
	}
	
	@Test
	public void shouldAccumulateTheFrequencyOnMultipleTrackEvents() throws Exception {
		TypeDistribution distribution = new TypeDistribution();
		
		distribution.trackTypeOccurrence("type", "1");
		distribution.trackTypeOccurrence("type", "1");
		
		assertThat(distribution.typeOccurrence("type"), equalTo(2.0));
		
	}
	
	@Test
	public void shouldExposeThePropertyOccurrence() throws Exception {
		TypeDistribution distribution = new TypeDistribution();
		
		distribution.trackPropertyOccurrenceForType("type", "1");
		
		assertThat(distribution.propertyOccurrenceForType("type"), equalTo(1.0));
	}
}
