package it.disco.unimib.test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import it.disco.unimib.index.Index;

import java.util.List;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class IndexTest {

	@Test
	public void whenALabelIsAddedItShouldBeReturned() throws Exception {
		Index index = new Index(new RAMDirectory()).add(new TripleBuilder()
															.withSubject("http://entity")
															.withPredicate("http://predicate")
															.withLiteral("the label").asTriple())
													.close();
		
		List<String> labels = index.get("http://entity");
		
		assertThat(labels, hasItem("\"the label\""));
	}
}