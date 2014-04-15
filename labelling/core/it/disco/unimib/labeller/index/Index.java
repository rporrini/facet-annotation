package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public abstract class Index{

	private IndexWriter writer;
	private Directory directory;
	private DirectoryReader reader;

	public Index(Directory directory) throws Exception{
		this.directory = directory;
		this.writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer())
																			.setRAMBufferSizeMB(95));
	}

	public List<String> get(String type, String context) throws Exception {
		ArrayList<String> results = new ArrayList<String>();
		IndexSearcher indexSearcher = new IndexSearcher(openReader());
		for(ScoreDoc score : indexSearcher.search(toQuery(type, context), 20).scoreDocs){
			results.add(toResult(indexSearcher.doc(score.doc)));
		}
		return results;
	}

	public Index closeWriter() throws Exception {
		this.writer.close();
		return this;
	}
	
	public Index closeReader() throws Exception {
		this.writer.close();
		return this;
	}
	
	public Index add(NTriple triple) throws Exception {
		writer.addDocument(toDocument(triple));
		return this;
	}
	
	private IndexReader openReader() throws Exception{
		if(reader == null){
			 reader = DirectoryReader.open(directory);
		}
		return reader;
	}
	
	protected abstract Analyzer analyzer();

	protected abstract String toResult(Document doc);

	protected abstract Query toQuery(String type, String context) throws Exception;

	protected abstract Document toDocument(NTriple triple) throws Exception;
}