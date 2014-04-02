package it.disco.unimib.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.baseline.RankedMajority;
import it.disco.unimib.baseline.TRankTypeRank;
import it.disco.unimib.labelling.HttpConnector;
import it.disco.unimib.labelling.Tagme;
import it.disco.unimib.labelling.TypeRanker;

import java.util.List;

import org.junit.Test;

public class TRankTypeRankTest {

	@Test
	public void tRankShouldGiveASetOfTypesGivenAnEntity() throws Exception {
		TypeRanker baseline = new TRankTypeRank(new RankedMajority());
		
		String type = baseline.typeOf("http://dbpedia.org/resource/Tom_Hanks", "http://dbpedia.org/resource/Harrison_Ford");
		
		assertThat(type, containsString("AmericanFilmActors"));
	}
	
	@Test
	public void tRankIntegrationTests() throws Exception {
		List<String> entities = new Tagme(new HttpConnector()).annotate("harrison ford", "tom hanks", "will smith");
		
		String type = new TRankTypeRank(new RankedMajority()).typeOf(entities.toArray(new String[entities.size()]));
		
		assertThat(type, is(not(nullValue())));
	}
}
