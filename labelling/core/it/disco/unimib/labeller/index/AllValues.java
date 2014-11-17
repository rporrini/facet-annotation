package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class AllValues {

	public Query createQuery(String value, String field, Analyzer analyzer) throws Exception {
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		parser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
		BooleanQuery query = new BooleanQuery();
		query.clauses().add(new BooleanClause(parser.parse(value, field), Occur.MUST));
		return query;
	}
}
