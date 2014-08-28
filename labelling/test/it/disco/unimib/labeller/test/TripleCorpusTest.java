package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.corpus.TripleCorpus;
import it.disco.unimib.labeller.index.EntityValues;
import it.disco.unimib.labeller.index.TripleIndex;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class TripleCorpusTest {

	@Test
	public void shouldWriteOutThePredicate() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		new TripleCorpus(new EntityValues(new RAMDirectory()).closeWriter(), file)
					.add(new TripleBuilder().withPredicate("http://example.org#thePredicate").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("http://example.org#thePredicate"));
	}
	
	@Test
	public void shouldWriteTheValueIfLiteralObject() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		new TripleCorpus(new EntityValues(new RAMDirectory()).closeWriter(), file)
					.add(new TripleBuilder().withLiteral("i am a literal value").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("i am a literal value"));
	}
	
	@Test
	public void shouldWriteTheLabelIfTheEntitesObjects() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		TripleIndex labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("the label")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(labels, file).add(new TripleBuilder().withObject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("the label"));
	}
}