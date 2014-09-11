package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;

public class AnyValue{
	
	public BooleanQuery createQuery(String value, String literalField, Analyzer analyzer) throws Exception {
		BooleanQuery query = new BooleanQuery();
		String escape = "(" + QueryParser.escape(value.replace("OR", "or").replace("AND", "and")) + ")";
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		parser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
		query.clauses().add(new BooleanClause(parser.parse(escape, literalField), Occur.MUST));
		return query;
	}
}