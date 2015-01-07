package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public class AllValues implements SingleFieldSelectionCriterion {

	private Analyzer analyzer;

	public AllValues(Analyzer analyzer){
		this.analyzer = analyzer;
	}
	
	public IndexQuery createQuery(String value, String field) throws Exception {
		IndexQuery query = new IndexQuery(this.analyzer).all().match(value, field);
		return query;
	}
}
