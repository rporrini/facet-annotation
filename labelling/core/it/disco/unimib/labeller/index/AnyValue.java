package it.disco.unimib.labeller.index;

import org.apache.lucene.analysis.Analyzer;

public class AnyValue implements FieldSelectionCriterion{
	
	private Analyzer analyzer;

	public AnyValue(Analyzer analyzer){
		this.analyzer = analyzer;
	}
	
	public Constraint createQuery(String value, String field) throws Exception {
		Constraint query = new Constraint(this.analyzer).any().match(value, field);
		return query;
	}
}