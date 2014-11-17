package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;

public class AllValues implements SingleFieldSelectionCriterion {

	public BooleanQuery createQuery(String value, String field, Analyzer analyzer) throws Exception {
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		parser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
		String parse = parser.parse(QueryParser.escape(value), field).toString();
		BooleanQuery query = new BooleanQuery();
		query.add(parser.parse(parse, field), Occur.MUST);
		return query;
	}
}
