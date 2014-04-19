package it.disco.unimib.labeller.index;


import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;

public class KeyValueStore extends AbstractIndex {

	public KeyValueStore(Directory directory) throws Exception {
		super(directory);
	}
	
	@Override
	protected List<Integer> matchingIds(String type, String context, IndexSearcher indexSearcher) throws Exception {
		List<Integer> ids = new ArrayList<Integer>();
		for(ScoreDoc score : indexSearcher.search(toQuery(type, context), maximumNumberOfTypesForAResourceInDBPedia()).scoreDocs){
			ids.add(score.doc);
		}
		return ids;
	}

	@Override
	protected Document toDocument(NTriple triple) {
		Document document = new Document();
		document.add(new Field(id(), triple.subject(), TextField.TYPE_STORED));
		document.add(new Field(value(), triple.object().toString(), TextField.TYPE_STORED));
		return document;
	}
	
	@Override
	protected String toResult(Document doc) {
		return doc.get(value());
	}
	
	@Override
	protected Analyzer analyzer() {
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
}
