package it.disco.unimib.labeller.index;


import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class EntityValues implements TripleStore{

	private IndexWriter writer;
	private Directory directory;
	private DirectoryReader reader;

	public EntityValues(Directory directory) throws Exception {
		this.directory = directory;
	}
	
	public List<CandidateResource> get(String type, String context) throws Exception {
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		IndexSearcher indexSearcher = new IndexSearcher(openReader());
		for(ScoreDoc documentPointer : matchingIds(type, context, indexSearcher)){
			Document indexedDocument = indexSearcher.doc(documentPointer.doc);
			results.add(new CandidateResource(toResult(indexedDocument), documentPointer.score));
		}
		return results;
	}

	public EntityValues closeWriter() throws Exception {
		openWriter().close();
		return this;
	}

	public EntityValues closeReader() throws Exception {
		this.reader.close();
		return this;
	}

	public EntityValues add(NTriple triple) throws Exception {
		Document document = new Document();
		document.add(new Field(id(), triple.subject(), TextField.TYPE_STORED));
		document.add(new Field(value(), triple.object().toString(), TextField.TYPE_STORED));
		openWriter().addDocument(document);
		return this;
	}
	
	private List<ScoreDoc> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
		List<ScoreDoc> ids = new ArrayList<ScoreDoc>();
		for(ScoreDoc score : indexSearcher.search(toQuery(type, context), maximumNumberOfTypesForAResourceInDBPedia()).scoreDocs){
			ids.add(score);
		}
		return ids;
	}

	private String toResult(Document doc) {
		return doc.get(value());
	}
	
	private Analyzer analyzer() {
		return new KeywordAnalyzer();
	}
	
	private int maximumNumberOfTypesForAResourceInDBPedia() {
		return 203;
	}
	
	private String id() {
		return "id";
	}
	
	private String value() {
		return "value";
	}
	
	private Query toQuery(String type, String context) throws QueryNodeException {
		return new StandardQueryParser(analyzer()).parse("\"" + type + "\"", id());
	}

	private synchronized IndexWriter openWriter() throws Exception {
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer())
																					.setRAMBufferSizeMB(95));
		}
		return writer;
	}

	private IndexReader openReader() throws Exception {
		if(reader == null){
			 reader = DirectoryReader.open(directory);
		}
		return reader;
	}
}
