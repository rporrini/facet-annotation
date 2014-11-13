package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.index.SelectionCriterion;

public class CandidatePredicatesReport implements Predicates{

	private Predicates predicates;

	public CandidatePredicatesReport(Predicates predicates){
		this.predicates = predicates;
	}
	
	@Override
	public Distribution forValues(String context, String[] values, SelectionCriterion query) throws Exception {
		return predicates.forValues(context, values, query);
	}
}