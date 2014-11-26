package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.ConstantSimilarity;

import org.junit.Test;

public class ConstantSimilarityTest {

	@Test
	public void shouldReturnAlways1() {
		
		ConstantSimilarity similarity = new ConstantSimilarity();
		
		assertThat(similarity.getSimilarity("any", "any other"),  equalTo(1.0f));
	}
}
