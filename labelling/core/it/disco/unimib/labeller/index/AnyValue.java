package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;

public class AnyValue implements SingleFieldSelectionCriterion{
	
	public BooleanQuery createQuery(String value, String literalField, Analyzer analyzer) throws Exception {
		String escape = QueryParser.escape(value.replace("OR", "or").replace("AND", "and"));
		IndexQuery query = new IndexQuery(analyzer).matchAny(escape, literalField);
		return query.build();
	}
}