package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.CandidatePredicate;
import it.disco.unimib.labeller.index.FullTextQuery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class CandidatePredicatesReport implements Predicates{

	private Predicates predicates;

	public CandidatePredicatesReport(Predicates predicates){
		this.predicates = predicates;
	}
	
	@Override
	public HashMap<String, List<CandidatePredicate>> forValues(String context, String[] values, FullTextQuery query) throws Exception {
		HashMap<String, List<CandidatePredicate>> results = predicates.forValues(context, values, query);
		logOverallStatistics(context, results);
		logOccurrenciesByValue(results);
		logOccurrenciesByPredicate(results);
		return results;
	}

	private void logOverallStatistics(String context, HashMap<String, List<CandidatePredicate>> results) {
		new Events().debug("CONTEXT|VALUES|MATCHED VALUES");
		int matchedValues = 0;
		for(Entry<String, List<CandidatePredicate>> value : results.entrySet()){
			if(!value.getValue().isEmpty()){
				matchedValues++;
			}
		}
		new Events().debug(context + "|" + results.size() + "|" + matchedValues);
	}
	
	private void logOccurrenciesByPredicate(HashMap<String, List<CandidatePredicate>> values) {
		HashSet<String> predicates = new HashSet<String>();
		new Events().debug("Predicate|Values|Number of values|Average score");
		for(String value : values.keySet()){
			for(CandidatePredicate predicate : values.get(value)){
				predicates.add(predicate.value());
			}
		}
		for(String predicate : predicates){
			String log = predicate + "|";
			int count = 0;
			double sum = 0;
			for(String value : values.keySet()){
				for(CandidatePredicate result : values.get(value)){
					if(predicate.equals(result.value())){
						log += value + "; ";
						sum += result.score();
						count++;
					}
				}
			}
			new Events().debug(log + "|" + count + "|" + sum/count);
		}
		new Events().debug("-------------------");
	}

	private void logOccurrenciesByValue(HashMap<String, List<CandidatePredicate>> values) {
		new Events().debug("Value (Number of predicates)|Predicates|Score");
		for(String value : values.keySet()){
			new Events().debug(value + " (" + values.get(value).size() + ")||");
			for(CandidatePredicate result : values.get(value)){
				new Events().debug("|" + result.value() + "|" + result.score());
			}
		}
	}
}