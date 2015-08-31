package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.corpus.TripleCorpus;
import it.disco.unimib.labeller.index.EntityValues;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class TripleCorpusTest {

	@Test
	public void shouldWriteOutThePredicate() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		EntityValues types = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder()
											.withSubject("http://the.subject")
											.withObject("http://the.type")
											.asTriple())
								.closeWriter();
		
		EntityValues labels = new EntityValues(new RAMDirectory())
								.add(new TripleBuilder()
											.withSubject("http://the.type")
											.withObject("label")
											.asTriple())
								.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer())
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withProperty("http://example.org#thePredicate")
											.asTriple());
		
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("http://example.org#thePredicate"));
	}

	@Test
	public void shouldWriteTheValueIfLiteralObject() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		EntityValues types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		EntityValues labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("label")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer())
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withLiteral("string")
											.asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("string"));
	}
	
	@Test
	public void shouldWriteTheLabelIfTheEntitesObjects() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		EntityValues labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("label")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("label")
												.asTriple())
									.closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer()).add(new TripleBuilder().withSubject("http://the.subject").withObject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("label"));
	}
	
	@Test
	public void shouldWriteTheLabelsOfAllTheCategoriesAndTypes() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		EntityValues labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("type label")
												.asTriple())
									.closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer()).add(new TripleBuilder().withSubject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(1));
		assertThat(file.getWrittenLines().get(0), containsString("type label"));
	}
	
	@Test
	public void shouldWriteMultipleSentencesForEachContextElement() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		EntityValues labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("the type label")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.category")
												.withObject("the category label")
												.asTriple())
									.closeWriter();
		EntityValues types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("http://the.type")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.entity")
												.withObject("http://the.category")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer()).add(new TripleBuilder().withSubject("http://the.entity").asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(2));
	}
	
	@Test
	public void literalValuesShouldBeNormalized() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		EntityValues labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("TYPE-label")
												.asTriple())
									.closeWriter();
		
		EntityValues types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer())
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withProperty("thePredicate")
											.withLiteral("2013-12-24")
											.asTriple());
		
		assertThat(file.getWrittenLines().get(0), is(equalTo("type label thePredicate 2013 12 24")));
	}
	
	@Test
	public void shouldWriteOutOnMultpleLabelsForTypesAndObjects() throws Exception {
		OutputFileTestDouble file = new OutputFileTestDouble();
		
		EntityValues labels = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.object")
												.withObject("object label")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.object")
												.withObject("another object label")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("type label")
												.asTriple())
									.add(new TripleBuilder()
												.withSubject("http://the.type")
												.withObject("another type label")
												.asTriple())
									.closeWriter();
		
		EntityValues types = new EntityValues(new RAMDirectory())
									.add(new TripleBuilder()
												.withSubject("http://the.subject")
												.withObject("http://the.type")
												.asTriple())
									.closeWriter();
		
		new TripleCorpus(types, labels, file, new EnglishAnalyzer())
					.add(new TripleBuilder().withSubject("http://the.subject")
											.withProperty("thePredicate")
											.withLiteral("http://the.object")
											.asTriple());
		
		assertThat(file.getWrittenLines(), hasSize(4));
	}
}