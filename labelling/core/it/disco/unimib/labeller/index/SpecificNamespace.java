package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

public class SpecificNamespace implements FullTextQuery {

	private FullTextQuery query;
	private String namespace;

	public SpecificNamespace(String namespace, FullTextQuery query) {
		this.query = query;
		this.namespace = namespace;
	}

	@Override
	public Query createQuery(String type, String context, String literalField, String contextField, String namespaceField, Analyzer analyzer) throws Exception {
		Query queryToDecorate = query.createQuery(type, context, literalField, contextField, namespaceField, analyzer);
		BooleanQuery result = new BooleanQuery();
		result.clauses().add(new BooleanClause(queryToDecorate, Occur.MUST));
		result.clauses().add(new BooleanClause(new StandardQueryParser(new KeywordAnalyzer()).parse(QueryParser.escape(namespace), namespaceField), Occur.MUST));
		return result;
	}

}
