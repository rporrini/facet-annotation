package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;

public class AllValues implements SingleFieldSelectionCriterion {

	public BooleanQuery createQuery(String value, String field, Analyzer analyzer) throws Exception {
		String escape = QueryParser.escape(value.replace("OR", "or").replace("AND", "and").replace("-", " "));
		IndexQuery builder = new IndexQuery(analyzer).all().match(escape, field);
		return builder.build();
	}
}
