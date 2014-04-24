package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

public interface FullTextQuery{
	public Query createQuery(String type, String context, String literalField, String contextField, Analyzer analyzer) throws Exception;
}