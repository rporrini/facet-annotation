package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;
import it.disco.unimib.labeller.benchmark.OrderedFacets;

import org.junit.Test;

public class OrderedFacetsTest {

	@Test
	public void shouldOrderByNameAndProvider() {
		GoldStandard goldStandard = new GoldStandardTestDouble()
											.withGroup("amazon_movies")
											.withGroup("pricegrabber_video")
											.withGroup("amazon_video")
											.withGroup("pricegrabber_movies");
		
		GoldStandardFacet[] facets = new OrderedFacets(goldStandard).getFacets();
		
		assertThat(facets[0].provider(), equalTo("amazon"));
		assertThat(facets[0].context(), equalTo("movies"));
		
		assertThat(facets[1].provider(), equalTo("amazon"));
		assertThat(facets[1].context(), equalTo("video"));
		
		assertThat(facets[2].provider(), equalTo("pricegrabber"));
		assertThat(facets[2].context(), equalTo("movies"));
		
		assertThat(facets[3].provider(), equalTo("pricegrabber"));
		assertThat(facets[3].context(), equalTo("video"));
	}
}