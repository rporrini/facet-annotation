package it.disco.unimib.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.mem0r1es.trank.TRanker;
import io.mem0r1es.trank.ranking.ANCESTORS;
import it.disco.unimib.baseline.TRankBaseline;

import java.net.URI;
import java.util.List;

import org.junit.Test;

import scala.collection.Seq;
import scala.collection.immutable.Map;

public class BaselineTest {

	@Test
	public void tRankStandaloneShouldRun() {
		Map<URI, Seq<URI>> types = new TRanker("University of Fribourg", new ANCESTORS()).entityToTRankedTypes();
		
		assertThat(types.size(), is(greaterThan(0)));
	}
	
	@Test
	public void tRankShouldGiveASetOfTypesGivenAnEntity() throws Exception {
		TRankBaseline baseline = new TRankBaseline();
		
		List<String> types = baseline.typesOf("http://dbpedia.org/resource/Tom_Hanks");
		
		assertThat(types, hasSize(greaterThan(1)));
	}
}
