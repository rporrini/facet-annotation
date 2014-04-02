package it.disco.unimib.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.benchmark.GoldStandardGroup;

import org.junit.Test;

public class GoldStandardGroupTest {

	@Test
	public void shouldReturnTheLabelOfTheGroupWhenThereIsAlsoAdditionalInformation() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																.withName("amazon_category_the label");
		
		GoldStandardGroup group = new GoldStandardGroup(connector);
		
		assertThat(group.label(), is(equalTo("the label")));
	}
	
	@Test
	public void shouldReturnTheContentOfTheGroup() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																			.withLine("1900")
																			.withLine("2000");
		
		GoldStandardGroup group = new GoldStandardGroup(connector);
		
		assertThat(group.elements(), allOf(hasItem("1900"), hasItem("2000")));
	}
	
	@Test
	public void shouldReturnTheNameOfTheSource() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																			.withName("amazon");
		
		GoldStandardGroup group = new GoldStandardGroup(connector);
		
		assertThat(group.provider(), is(equalTo("amazon")));
	}
}