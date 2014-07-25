package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;

public class AllValues{
	public BooleanQuery createQuery(String value, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		BooleanQuery query = new BooleanQuery();
		String escape = "(" + QueryParser.escape(value) + ")";
		StandardQueryParser standardQueryParser1 = new StandardQueryParser(analyzer);
		standardQueryParser1.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
		query.clauses().add(new BooleanClause(standardQueryParser1.parse(escape, literalField), Occur.MUST));
		return query;
	}
}