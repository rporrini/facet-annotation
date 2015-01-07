package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.util.Version;

public class IndexQuery {

	private BooleanQuery query;
	private Analyzer analyzer;

	public IndexQuery(){
		this(new StandardAnalyzer(Version.LUCENE_45));
	}
	
	public IndexQuery(Analyzer analyzer){
		this.query = new BooleanQuery();
		this.analyzer = analyzer;
	}
	
	public BooleanQuery build() {
		return query;
	}

	public IndexQuery matchExactly(String value, String field) {
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(field, value));
		query.add(phraseQuery, Occur.MUST);
		return this;
	}

	public IndexQuery matchAll(String value, String field) throws Exception {
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		parser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
		query.clauses().add(new BooleanClause(parser.parse(value, field), Occur.MUST));
		return this;
	}

	public IndexQuery matchAny(String value, String field) throws Exception {
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		parser.setDefaultOperator(StandardQueryConfigHandler.Operator.OR);
		query.clauses().add(new BooleanClause(parser.parse(value, field), Occur.MUST));
		return this;
	}
}
