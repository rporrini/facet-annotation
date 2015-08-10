package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.EntityValues;

import java.util.List;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class EntityValuesTest {

	@Test
	public void whenALabelIsAddedItShouldBeReturned() throws Exception {
		EntityValues index = new EntityValues(new RAMDirectory()).add(new TripleBuilder()
															.withSubject("http://entity")
															.withLiteral("the label").asTriple())
													.closeWriter();
		
		List<CandidateProperty> labels = index.get("http://entity");
		
		assertThat(labels.get(0).uri(), equalTo("the label"));
	}
	
	@Test
	public void whenADuplicatedEntityIsAddedShouldReturnBoth() throws Exception {
		EntityValues index = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder().withSubject("http://entity").withLiteral("the label").asTriple())
								.add(new TripleBuilder().withSubject("http://entity").withLiteral("the other label").asTriple())
						.closeWriter();

		List<CandidateProperty> labels = index.get("http://entity");
		
		assertThat(labels, hasSize(2));
	}
}