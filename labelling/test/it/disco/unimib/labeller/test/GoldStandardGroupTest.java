package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardGroup;

import org.junit.Test;

public class GoldStandardGroupTest {

	@Test
	public void shouldReturnTheLabelOfTheGroupWhenThereIsAlsoAdditionalInformation() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																.withName("ecommerce_amazon_category_the label");
		
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
																			.withName("ecommerce_amazon");
		
		GoldStandardGroup group = new GoldStandardGroup(connector);
		
		assertThat(group.provider(), is(equalTo("amazon")));
	}
	
	@Test
	public void shouldReturnTheContext() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																			.withName("ecommerce_amazon_category");
		
		GoldStandardGroup group = new GoldStandardGroup(connector);
		
		assertThat(group.context(), is(equalTo("category")));
	}
	
	@Test
	public void shouldGetTheDomain() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																			.withName("ecommerce");

		GoldStandardGroup group = new GoldStandardGroup(connector);

		assertThat(group.domain(), is(equalTo("ecommerce")));
	}
	
	@Test
	public void shouldGetTheHyperlink() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																			.withName("ecommerce_amazon_context_label_list_of_wines");

		GoldStandardGroup group = new GoldStandardGroup(connector);

		assertThat(group.hyperlink(), is(equalTo("list_of_wines")));
	}
	
	@Test
	public void shouldSkipEmptyLines() throws Exception {
		FileSystemConnectorTestDouble connector = new FileSystemConnectorTestDouble()
																			.withLine("first line")
																			.withLine("")
																			.withLine("second line");
		
		GoldStandardGroup group = new GoldStandardGroup(connector);
		
		assertThat(group.elements(), hasSize(2));
	}
}