package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class OptionalContext implements FullTextQuery{
	
	public Query createQuery(String type, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		BooleanQuery query = new BooleanQuery();
		String escape = "(" + QueryParser.escape(type) + ")";
		StandardQueryParser standardQueryParser = new StandardQueryParser(analyzer);
		standardQueryParser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
		query.clauses().add(new BooleanClause(standardQueryParser.parse(escape, literalField), Occur.MUST));
		standardQueryParser.setDefaultOperator(StandardQueryConfigHandler.Operator.OR);
		query.clauses().add(new BooleanClause(standardQueryParser.parse(QueryParser.escape(context), contextField), Occur.SHOULD));
		return query;
	}
}