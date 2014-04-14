package it.disco.unimib.labeller.index;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;

public class KeyValueStore extends Index {

	public KeyValueStore(Directory directory) throws Exception {
		super(directory);
	}

	@Override
	protected Document toDocument(NTriple triple) {
		Document document = new Document();
		document.add(new Field(id(), triple.subject(), TextField.TYPE_STORED));
		document.add(new Field(value(), triple.object().toString(), TextField.TYPE_STORED));
		return document;
	}
	
	@Override
	protected Query toQuery(String type) throws QueryNodeException {
		return new StandardQueryParser(analyzer()).parse("\"" + type + "\"", id());
	}
	
	@Override
	protected String toResult(Document doc) {
		return doc.get(value());
	}
	
	@Override
	protected Analyzer analyzer() {
		return new KeywordAnalyzer();
	}
	
	private String id() {
		return "id";
	}
	
	private String value() {
		return "value";
	}
}
