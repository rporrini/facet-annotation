package it.disco.unimib.labeller.index;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;

public class EntityValues implements ReadAndWriteStore{

	private IndexWriter writer;
	private IndexSearcher searcher;
	private Directory directory;
	
	private HashSet<String> fieldsToLoad;

	public EntityValues(Directory directory) throws Exception {
		this.directory = directory;
		this.fieldsToLoad = new HashSet<String>(Arrays.asList(new String[]{value()}));
	}
	
	public List<CandidateProperty> get(String entity) throws Exception {
		Query query = new Constraint().matchExactly(entity, id()).build();
		IndexSearcher searcher = openSearcher();
		
		ArrayList<CandidateProperty> results = new ArrayList<CandidateProperty>();
		for(ScoreDoc score : searcher.search(query, 500, Sort.INDEXORDER).scoreDocs){
			Document document = openSearcher().doc(score.doc, fieldsToLoad);
			results.add(new CandidateProperty(document.get(value())));
		}
		return results;
	}

	public EntityValues closeWriter() throws Exception {
		openWriter().close();
		return this;
	}

	public EntityValues closeReader() throws Exception {
		this.openSearcher().getIndexReader().close();
		return this;
	}

	public EntityValues add(NTriple triple) throws Exception {
		Document document = new Document();
		document.add(new Field(id(), triple.subject().uri(), TextField.TYPE_STORED));
		document.add(new Field(value(), triple.object().uri(), TextField.TYPE_STORED));
		openWriter().addDocument(document);
		return this;
	}
	
	private String id() {
		return "id";
	}
	
	private String value() {
		return "value";
	}
	
	private synchronized IndexWriter openWriter() throws Exception {
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(new KeywordAnalyzer()).setRAMBufferSizeMB(95));
		}
		return writer;
	}

	private synchronized IndexSearcher openSearcher() throws Exception {
		if(searcher == null){
			 searcher = new IndexSearcher(DirectoryReader.open(directory));
		}
		return searcher;
	}
}
