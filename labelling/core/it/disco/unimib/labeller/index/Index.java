package it.disco.unimib.labeller.index;

import java.util.List;

public interface Index {

	public List<CandidatePredicate> get(String value, String context, SelectionCriterion query) throws Exception;

	public long count(String predicate, String context, SelectionCriterion query) throws Exception;
}