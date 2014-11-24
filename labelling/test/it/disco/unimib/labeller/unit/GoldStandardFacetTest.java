package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.benchmark.GoldStandardFacet;

import org.junit.Test;

public class GoldStandardFacetTest {

	@Test
	public void shouldReturnTheLabelOfTheGroupWhenThereIsAlsoAdditionalInformation() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																.withName("amazon_category_the label");
		
		GoldStandardFacet facet = new GoldStandardFacet(connector);
		
		assertThat(facet.label(), is(equalTo("the label")));
	}
	
	@Test
	public void shouldReturnTheContentOfTheGroup() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																			.withLine("1900")
																			.withLine("2000");
		
		GoldStandardFacet facet = new GoldStandardFacet(connector);
		
		assertThat(facet.elements(), allOf(hasItem("1900"), hasItem("2000")));
	}
	
	@Test
	public void shouldReturnTheNameOfTheSource() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																			.withName("amazon");
		
		GoldStandardFacet facet = new GoldStandardFacet(connector);
		
		assertThat(facet.provider(), is(equalTo("amazon")));
	}
	
	@Test
	public void shouldReturnTheContext() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																			.withName("amazon_category");
		
		GoldStandardFacet facet = new GoldStandardFacet(connector);
		
		assertThat(facet.context(), is(equalTo("category")));
	}
	
	@Test
	public void shouldGetTheHyperlink() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																			.withName("amazon_context_label_list_of_wines");

		GoldStandardFacet facet = new GoldStandardFacet(connector);

		assertThat(facet.contextHyperlink(), is(equalTo("list_of_wines")));
	}
	
	@Test
	public void shouldSkipEmptyLines() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																			.withLine("first line")
																			.withLine("")
																			.withLine("second line");
		
		GoldStandardFacet facet = new GoldStandardFacet(connector);
		
		assertThat(facet.elements(), hasSize(2));
	}
	
	@Test
	public void shouldSkipLinesWithHash() throws Exception {
		InputFileTestDouble connector = new InputFileTestDouble()
																			.withLine("first line")
																			.withLine("#second line")
																			.withLine("third line");
		
		GoldStandardFacet facet = new GoldStandardFacet(connector);
		
		assertThat(facet.elements(), not(hasItem("#second line")));
	}
}