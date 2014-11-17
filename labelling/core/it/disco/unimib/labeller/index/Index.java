package it.disco.unimib.labeller.index;

import java.util.List;

public interface Index {

	public List<CandidatePredicate> get(String value, String context, TripleSelectionCriterion query) throws Exception;

	public long countPredicatesInContext(String predicate, String context, TripleSelectionCriterion query) throws Exception;
}