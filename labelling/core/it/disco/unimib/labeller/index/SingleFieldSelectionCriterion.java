package it.disco.unimib.labeller.index;


public interface SingleFieldSelectionCriterion {

	public Constraint createQuery(String value, String field) throws Exception;

}