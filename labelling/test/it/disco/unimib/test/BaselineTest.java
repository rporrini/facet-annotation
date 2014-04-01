package it.disco.unimib.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.baseline.TRankBaseline;

import java.util.List;

import org.junit.Test;

public class BaselineTest {

	@Test
	public void tRankShouldGiveASetOfTypesGivenAnEntity() throws Exception {
		TRankBaseline baseline = new TRankBaseline();
		
		List<String> types = baseline.typesOf("http://dbpedia.org/resource/Tom_Hanks", "http://dbpedia.org/resource/Harrison_Ford");
		
		assertThat(types, hasSize(greaterThan(1)));
	}
}
