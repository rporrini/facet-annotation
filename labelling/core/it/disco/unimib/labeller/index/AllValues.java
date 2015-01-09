package it.disco.unimib.labeller.index;


public class AllValues implements SingleFieldSelectionCriterion {

	private IndexFields analyzer;

	public AllValues(IndexFields analyzer){
		this.analyzer = analyzer;
	}
	
	public Constraint createQuery(String value, String field) throws Exception {
		Constraint query = analyzer.toConstraint().all().match(value, field);
		return query;
	}
}
