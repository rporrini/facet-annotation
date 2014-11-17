package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import org.apache.commons.lang3.StringUtils;

public class CandidatePredicatesReport implements Predicates{

	private Predicates predicates;

	public CandidatePredicatesReport(Predicates predicates){
		this.predicates = predicates;
	}
	
	@Override
	public Distribution forValues(String context, String[] values, TripleSelectionCriterion query) throws Exception {
		log("processing " + context + " [" + StringUtils.join(values, ", ") + "]");
		
		Distribution distribution = predicates.forValues(context, values, query);
		String header = "|";
		for(String value : distribution.values()){
			header += value + "|";
		}
		log(header);
		for(String predicate : distribution.predicates()){
			String line = predicate + "|";
			for(String value : distribution.values()){
				line += distribution.scoreOf(predicate, value) + "|";
			}
			log(line);
		}
		return distribution;
	}

	private void log(String header) {
		new Events().debug(">" + header);
	}
}