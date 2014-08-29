package it.disco.unimib.labeller.test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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
											.withLiteral("string")
											.asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("string"));
	}
	
	@Test
	public void shouldWriteTheLabelIfTheEntitesObjects() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		TripleIndex labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("label")
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
		assertThat(file.getWrittenLines().get(0), containsString("label"));
	}
	
	@Test
	public void shouldWriteTheLabelsOfAllTheCategoriesAndTypes() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		TripleIndex labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("type label")
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
		assertThat(file.getWrittenLines().get(0), containsString("type label"));
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
	
	@Test
	public void literalValuesShouldBeNormalized() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		TripleIndex labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("TYPE-label")
												.asTriple())
									.closeWriter();
		
		TripleIndex types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file)
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withPredicate("thePredicate")
											.withLiteral("2013-12-24")
											.asTriple());
		
		assertThat(file.getWrittenLines().get(0), is(equalTo("type label thePredicate 2013 12 24")));
	}
	
	private TripleIndex emptyIndex() throws Exception {
		return new EntityValues(new RAMDirectory()).closeWriter();
	}
}