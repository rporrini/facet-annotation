package it.disco.unimib.test;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.mem0r1es.trank.TRanker;
import io.mem0r1es.trank.ranking.ANCESTORS;

import java.net.URI;

import org.junit.Test;

import scala.collection.Seq;
import scala.collection.immutable.Map;

public class BaselineTest {

	@Test
	public void theBaselineShouldRun() {
		Map<URI, Seq<URI>> types = new TRanker("University of Fribourg", new ANCESTORS()).entityToTRankedTypes();
		
		assertThat(types.size(), is(greaterThan(0)));
	}

}
