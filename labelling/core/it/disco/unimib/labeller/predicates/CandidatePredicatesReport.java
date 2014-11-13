package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.HashMap;
import java.util.List;

public class CandidatePredicatesReport implements Predicates{

	private Predicates predicates;

	public CandidatePredicatesReport(Predicates predicates){
		this.predicates = predicates;
	}
	
	@Override
	public HashMap<String, List<CandidatePredicate>> forValues(String context, String[] values, SelectionCriterion query) throws Exception {
		HashMap<String, List<CandidatePredicate>> results = predicates.forValues(context, values, query);
		return results;
	}
}