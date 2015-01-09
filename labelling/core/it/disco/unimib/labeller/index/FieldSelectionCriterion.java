package it.disco.unimib.labeller.index;


public interface FieldSelectionCriterion {

	public Constraint createQuery(String value, String field) throws Exception;

}