package it.disco.unimib.test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labelling.HttpConnector;
import it.disco.unimib.labelling.Tagme;

import java.util.List;

import org.junit.Test;

public class TagmeTest {

	@Test
	public void onEmptyResposeShouldGiveAnEmptyEntitySet() throws Exception {
		Tagme tagme = new Tagme(new TagmeConnectorTestDouble());
		
		assertThat(tagme.annotate("any"), is(empty()));
	}
	
	@Test
	public void onOneSingleEntityShouldReturnIt() throws Exception {
		TagmeConnectorTestDouble connector = new TagmeConnectorTestDouble().thatReturns("type");
		
		List<String> types = new Tagme(connector).annotate("any");
		
		assertThat(types, hasItem("http://dbpedia.org/resource/type"));
	}
	
	@Test
	public void onManyAnnotationsShouldReturnManyInstances() throws Exception {
		TagmeConnectorTestDouble connector = new TagmeConnectorTestDouble().thatReturns("type").thatReturns("other-type");
		
		List<String> types = new Tagme(connector).annotate("any");
		
		assertThat(types, hasSize(2));
	}
	
	@Test
	public void shouldConvertTheNameToDBPediaOne() throws Exception {
		TagmeConnectorTestDouble connector = new TagmeConnectorTestDouble().thatReturns("entity with a space");
		
		List<String> types = new Tagme(connector).annotate("any");
		
		assertThat(types, hasItem("http://dbpedia.org/resource/entity_with_a_space"));
	}
	
	@Test
	public void shouldComposeTheRighFormat() throws Exception {
		TagmeConnectorTestDouble connector = new TagmeConnectorTestDouble();
		
		new Tagme(connector).annotate("foo", "baz", "bar");
		
		assertThat(connector.lastRequest(), is(equalTo("http://tagme.di.unipi.it/tag?text=foo%2C+baz%2C+bar&key=ricpor2014&lang=en&include_categories=true")));
	}
	
	@Test
	public void tagMeIntegrationTest() throws Exception {
		
		List<String> entities = new Tagme(new HttpConnector()).annotate("tom hanks", "harrison ford");
		
		assertThat(entities, hasSize(2));
	}
}