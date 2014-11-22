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

public abstract class TripleIndex implements TripleStore{

	private IndexWriter writer;
	private Directory directory;
	private DirectoryReader reader;

	public TripleIndex(Directory directory) throws Exception{
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

	public TripleIndex closeWriter() throws Exception {
		openWriter().close();
		return this;
	}
	
	public TripleStore closeReader() throws Exception {
		this.reader.close();
		return this;
	}
	
	@Override
	public TripleIndex add(NTriple triple) throws Exception {
		openWriter().addDocument(toDocument(triple));
		return this;
	}
	
	private synchronized IndexWriter openWriter() throws Exception{
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, analyzer())
																					.setRAMBufferSizeMB(95));
		}
		return writer;
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