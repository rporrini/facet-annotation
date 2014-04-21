package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.labelling.Normalize;

import org.junit.Test;

public class NormalizeTest {

	@Test
	public void shouldNormalizeASetOfASingleProbability() {
		Normalize normalize = new Normalize(new Double[]{0.66});
		
		assertThat(normalize.value(0.66), equalTo(1.0d));
	}
}