package it.disco.unimib.labeller.index;


public class AllValues implements FieldSelectionCriterion {

	private IndexFields analyzer;

	public AllValues(IndexFields analyzer){
		this.analyzer = analyzer;
	}
	
	public Constraint createQuery(String value, String field) throws Exception {
		return analyzer.toConstraint().all().match(value, field);
	}
}
