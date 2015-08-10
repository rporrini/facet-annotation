package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.properties.CosineSimilarity;
import it.disco.unimib.labeller.properties.TypeDistribution;

import org.junit.Test;

public class CosineSimilarityTest {

	@Test
	public void theSimilarityBetweenTwoEmptyTypeDistributionsShouldBeMinimal() {
		TypeDistribution firstDistribution = new TypeDistribution();
		TypeDistribution secondDistribution = new TypeDistribution();
		
		double similarity = new CosineSimilarity().between(firstDistribution, secondDistribution);
		
		assertThat(similarity, equalTo(0.0));
	}
	
	@Test
	public void theSimilarityBetweenTheSameDistributionsShouldBeMaximal() throws Exception {
		TypeDistribution distribution = new TypeDistribution()
													.trackPropertyOccurrenceForType("type", "1")
													.trackTypeOccurrence("type", "1");
		
		double similarity = new CosineSimilarity().between(distribution, distribution);
		
		assertThat(similarity, equalTo(1.0));
	}
}
