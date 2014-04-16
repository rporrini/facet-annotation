package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandard;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;
import it.disco.unimib.labeller.benchmark.OrderedGroups;

import org.junit.Test;

public class OrderedGroupsTest {

	@Test
	public void shouldOrderByNameAndProvider() {
		GoldStandard goldStandard = new GoldStandardTestDouble()
											.withGroup("ecommerce_zzzz")
											.withGroup("music_zzzz")
											.withGroup("music_amazon")
											.withGroup("ecommerce_amazon");
		
		GoldStandardGroup[] groups = new OrderedGroups(goldStandard).getGroups();
		
		assertThat(groups[0].domain(), equalTo("ecommerce"));
		assertThat(groups[0].provider(), equalTo("amazon"));
		
		assertThat(groups[1].domain(), equalTo("ecommerce"));
		assertThat(groups[1].provider(), equalTo("zzzz"));
		
		assertThat(groups[2].domain(), equalTo("music"));
		assertThat(groups[2].provider(), equalTo("amazon"));
		
		assertThat(groups[3].domain(), equalTo("music"));
		assertThat(groups[3].provider(), equalTo("zzzz"));
	}
}