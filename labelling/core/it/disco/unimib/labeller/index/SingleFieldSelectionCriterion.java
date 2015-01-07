package it.disco.unimib.labeller.index;


public interface SingleFieldSelectionCriterion {

	public IndexQuery createQuery(String value, String field) throws Exception;

}