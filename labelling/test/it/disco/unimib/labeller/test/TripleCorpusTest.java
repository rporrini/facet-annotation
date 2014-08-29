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
		
		TripleIndex types = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder()
											.withSubject("http://the.subject")
											.withObject("http://the.type")
											.asTriple())
								.closeWriter();
		
		new TripleCorpus(types, emptyIndex(), file)
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withPredicate("http://example.org#thePredicate")
											.asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("http://example.org#thePredicate"));
	}

	@Test
	public void shouldWriteTheValueIfLiteralObject() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		TripleIndex types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, emptyIndex(), file)
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withLiteral("i am a literal value")
											.asTriple());
		
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
		TripleIndex types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file).add(new TripleBuilder().withSubject("http://the.subject").withObject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("the label"));
	}
	
	@Test
	public void shouldWriteTheLabelsOfAllTheCategoriesAndTypes() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		TripleIndex labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("the type label")
												.asTriple())
									.closeWriter();
		TripleIndex types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file).add(new TripleBuilder().withSubject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("the type label"));
	}
	
	@Test
	public void shouldWriteMultipleSentencesForEachContextElement() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		TripleIndex labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("the type label")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.category")
												.withObject("the category label")
												.asTriple())
									.closeWriter();
		TripleIndex types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("http://the.type")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("http://the.category")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file).add(new TripleBuilder().withSubject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(2));
	}
	
	private TripleIndex emptyIndex() throws Exception {
		return new EntityValues(new RAMDirectory()).closeWriter();
	}
}