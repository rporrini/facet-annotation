package it.disco.unimib.test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import io.mem0r1es.trank.TRanker;

import org.junit.Test;

import scala.collection.Seq;
import scala.collection.immutable.Map;

public class BaselineTest {

	@Test
	public void theBaselineShouldRun() {
		Map<URI, Seq<URI>> types = new TRanker("University of Fribourg").entityToTRankedTypes();
		
		assertThat(types.size(), is(greaterThan(0)));
	}

}
