package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.HashMap;
import java.util.List;

public interface Predicates {

	public HashMap<String, List<CandidatePredicate>> forValues(String context, String[] values, SelectionCriterion query) throws Exception;

}