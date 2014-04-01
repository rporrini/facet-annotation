package it.disco.unimib.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import it.disco.unimib.baseline.TRankBaseline;

import org.junit.Test;

public class BaselineTest {

	@Test
	public void tRankShouldGiveASetOfTypesGivenAnEntity() throws Exception {
		TRankBaseline baseline = new TRankBaseline();
		
		String type = baseline.typeOf("http://dbpedia.org/resource/Tom_Hanks", "http://dbpedia.org/resource/Harrison_Ford");
		
		assertThat(type, containsString("AmericanFilmActors"));
	}
}
