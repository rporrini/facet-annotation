package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class IndexQuery {

	private Operator operator;
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

	public IndexQuery all(){
		operator = StandardQueryConfigHandler.Operator.AND;
		return this;
	}
	
	public IndexQuery any(){
		operator = StandardQueryConfigHandler.Operator.OR;
		return this;
	}
	
	public IndexQuery matchExactly(String value, String field) throws Exception {
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(new Term(field, value));
		return addToQuery(phraseQuery) ;
	}
	
	public IndexQuery match(String value, String field) throws Exception {
		return addToQuery(multiWord(value, field, operator));
	}

	private IndexQuery addToQuery(Query query) throws Exception {
		this.query.add(query, Occur.MUST);
		return this;
	}

	private Query multiWord(String value, String field, Operator operator) throws Exception {
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		parser.setDefaultOperator(operator);
		String escape = QueryParser.escape(value.replace("OR", "or").replace("AND", "and").replace("-", " "));
		return parser.parse(escape, field);
	}
}
