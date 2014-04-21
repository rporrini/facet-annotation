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
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public abstract class AbstractIndex implements Index{

	private IndexWriter writer;
	private Directory directory;
	private DirectoryReader reader;

	public AbstractIndex(Directory directory) throws Exception{
		this.directory = directory;
		this.writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer())
																			.setRAMBufferSizeMB(95));
	}

	@Override
	public List<SearchResult> get(String type, String context) throws Exception {
		ArrayList<SearchResult> results = new ArrayList<SearchResult>();
		IndexSearcher indexSearcher = new IndexSearcher(openReader());
		for(ScoreDoc documentPointer : matchingIds(type, context, indexSearcher)){
			Document indexedDocument = indexSearcher.doc(documentPointer.doc);
			results.add(new SearchResult(toResult(indexedDocument), documentPointer.score));
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
	
	public AbstractIndex add(NTriple triple) throws Exception {
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

	protected abstract Document toDocument(NTriple triple) throws Exception;
	
	protected abstract List<ScoreDoc> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception;
}