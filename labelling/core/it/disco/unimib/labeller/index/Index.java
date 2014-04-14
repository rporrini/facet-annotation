package it.disco.unimib.labeller.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public abstract class Index {

	private IndexWriter writer;
	private Directory directory;

	public Index(Directory directory) throws Exception{
		this.directory = directory;
		this.writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer()));
	}

	public List<String> get(String type, String context) throws Exception {
		DirectoryReader reader = DirectoryReader.open(directory);
		ArrayList<String> results = new ArrayList<String>();
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		for(ScoreDoc score : indexSearcher.search(toQuery(type), Integer.MAX_VALUE).scoreDocs){
			results.add(toResult(indexSearcher.doc(score.doc)));
		}
		reader.close();
		return results;
	}

	public Index close() throws IOException {
		this.writer.close();
		return this;
	}

	public Index add(NTriple triple) throws Exception {
		writer.addDocument(toDocument(triple));
		return this;
	}
	
	protected abstract Analyzer analyzer();

	protected abstract String toResult(Document doc);

	protected abstract Query toQuery(String type) throws QueryNodeException;

	protected abstract Document toDocument(NTriple triple);
}