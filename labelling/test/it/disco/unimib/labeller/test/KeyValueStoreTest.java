package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.KeyValueStore;
import it.disco.unimib.labeller.index.Result;

import java.util.List;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class KeyValueStoreTest {

	@Test
	public void whenALabelIsAddedItShouldBeReturned() throws Exception {
		Index index = new KeyValueStore(new RAMDirectory()).add(new TripleBuilder()
															.withSubject("http://entity")
															.withLiteral("the label").asTriple())
													.closeWriter();
		
		List<Result> labels = index.get("http://entity", "any");
		
		assertThat(labels.get(0).value(), equalTo("the label"));
	}
	
	@Test
	public void whenADuplicatedEntityIsAddedShouldReturnBoth() throws Exception {
		Index index = new KeyValueStore(new RAMDirectory())
								.add(new TripleBuilder().withSubject("http://entity").withLiteral("the label").asTriple())
								.add(new TripleBuilder().withSubject("http://entity").withLiteral("the other label").asTriple())
						.closeWriter();

		List<Result> labels = index.get("http://entity", "any");
		
		assertThat(labels, hasSize(2));
	}
}