package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.IndexQuery;

import org.junit.Test;

public class IndexQueryTest {

	@Test
	public void onDefaultShouldBuildAnEmptyQuery() {
		
		IndexQuery builder = new IndexQuery();
		
		assertThat(builder.build(), is(notNullValue()));
	}
	
	@Test
	public void shouldMatchAnExactTerm() throws Exception {
		
		IndexQuery builder = new IndexQuery().matchExactly("http://term", "field");
		
		assertThat(builder.build().toString(), is("+field:\"http://term\""));
	}
}
