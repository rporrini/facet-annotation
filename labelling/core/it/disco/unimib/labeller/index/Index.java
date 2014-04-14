package it.disco.unimib.labeller.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class Index {

	private Directory directory;
	private IndexWriter writer;

	public Index(Directory directory) throws Exception{
		this.directory = directory;
		this.writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer()));
	}
	
	public List<String> get(String type) throws Exception {
		DirectoryReader reader = DirectoryReader.open(directory);
		ArrayList<String> results = new ArrayList<String>();
		for(ScoreDoc score : searchFor(type, reader)){
			results.add(new IndexSearcher(reader).doc(score.doc).get(value()));
		}
		reader.close();
		return results;
	}

	public Index close() throws IOException{
		this.writer.close();
		return this;
	}

	public Index add(NTriple triple) throws Exception {
		Document document = new Document();
		document.add(new Field(id(), triple.subject(), TextField.TYPE_STORED));
		document.add(new Field(value(), triple.object().toString(), TextField.TYPE_STORED));
		writer.addDocument(document);
		return this;
	}
	
	private ScoreDoc[] searchFor(String type, DirectoryReader reader) throws Exception {
		return new IndexSearcher(reader).search(new StandardQueryParser(analyzer()).parse("\"" + type + "\"", id()), Integer.MAX_VALUE).scoreDocs;
	}

	private String id() {
		return "id";
	}
	
	private String value() {
		return "value";
	}
	
	private KeywordAnalyzer analyzer() {
		return new KeywordAnalyzer();
	}
}