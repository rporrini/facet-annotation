package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.SingleFacet;

import org.junit.Test;

public class SingleFacetTest {

	@Test
	public void shouldFilterOverTheGroupName() throws Exception {
		GoldStandardTestDouble goldStandard = new GoldStandardTestDouble().withGroup("name");
		
		GoldStandard singleFacet = new SingleFacet(goldStandard, "name".hashCode());
		
		assertThat(singleFacet.getFacets().length, equalTo(1));
	}
}
