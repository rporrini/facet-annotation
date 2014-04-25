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
											.withGroup("amazon_movies")
											.withGroup("pricegrabber_video")
											.withGroup("amazon_video")
											.withGroup("pricegrabber_movies");
		
		GoldStandardGroup[] groups = new OrderedGroups(goldStandard).getGroups();
		
		assertThat(groups[0].provider(), equalTo("amazon"));
		assertThat(groups[0].context(), equalTo("movies"));
		
		assertThat(groups[1].provider(), equalTo("amazon"));
		assertThat(groups[1].context(), equalTo("video"));
		
		assertThat(groups[2].provider(), equalTo("pricegrabber"));
		assertThat(groups[2].context(), equalTo("movies"));
		
		assertThat(groups[3].provider(), equalTo("pricegrabber"));
		assertThat(groups[3].context(), equalTo("video"));
	}
}