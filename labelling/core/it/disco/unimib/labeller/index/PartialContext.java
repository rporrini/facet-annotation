package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;

public class PartialContext implements SelectionCriterion {

	@Override
	public BooleanQuery asQuery(String value, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		BooleanQuery query = new AllValues().createQuery(value, literalField, analyzer);
		
		StandardQueryParser standardQueryParser = new StandardQueryParser(analyzer);
		standardQueryParser.setDefaultOperator(StandardQueryConfigHandler.Operator.OR);
		query.clauses().add(new BooleanClause(standardQueryParser.parse(QueryParser.escape(context), contextField), Occur.MUST));
		return query;
	}
}
