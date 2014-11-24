package it.disco.unimib.labeller.index;


import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class EntityValues implements TripleStore{

	private IndexWriter writer;
	private Directory directory;
	private DirectoryReader reader;

	public EntityValues(Directory directory) throws Exception {
		this.directory = directory;
	}
	
	public List<CandidateResource> get(String entity) throws Exception {
		IndexSearcher searcher = new IndexSearcher(openReader());
		Query query = new TermQuery(new Term(id(), entity));
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(ScoreDoc score : searcher.search(query, maximumNumberOfTypesForAResourceInDBPedia()).scoreDocs){
			Document indexedDocument = searcher.doc(score.doc);
			results.add(new CandidateResource(indexedDocument.get(value()), score.score));
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
	
	private int maximumNumberOfTypesForAResourceInDBPedia() {
		return 203;
	}
	
	private String id() {
		return "id";
	}
	
	private String value() {
		return "value";
	}
	
	private synchronized IndexWriter openWriter() throws Exception {
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, new KeywordAnalyzer()).setRAMBufferSizeMB(95));
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
