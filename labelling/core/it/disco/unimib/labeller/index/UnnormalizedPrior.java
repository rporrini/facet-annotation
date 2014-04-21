package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UnnormalizedPrior {

	private HashMap<String, HashMap<String, List<SearchResult>>> predicatesDistribution;
	private Set<String> values;

	public UnnormalizedPrior(HashMap<String, List<SearchResult>> valueDistribution) {
		this.predicatesDistribution = invert(valueDistribution);
		this.values = valueDistribution.keySet();
	}

	private HashMap<String, HashMap<String, List<SearchResult>>> invert(HashMap<String, List<SearchResult>> valueDistribution) {
		HashMap<String, HashMap<String, List<SearchResult>>> inverted = new HashMap<String, HashMap<String, List<SearchResult>>>();
		for(String value : valueDistribution.keySet()){
			for(SearchResult predicate : valueDistribution.get(value)){
				if(!inverted.containsKey(predicate.value())) {
					inverted.put(predicate.value(), new HashMap<String, List<SearchResult>>());
				}
				if(!inverted.get(predicate.value()).containsKey(value)){
					inverted.get(predicate.value()).put(value, new ArrayList<SearchResult>());
				}
				inverted.get(predicate.value()).get(value).add(predicate);
			}
		}
		return inverted;
	}

	public double of(String predicate) {
		double totalScore = 0.0;
		for(String value : this.values){
			List<SearchResult> list = this.predicatesDistribution.get(predicate).get(value);
			double count = 0.0;
			if(list != null){
				for(SearchResult result : list){
					count += result.score();
				}
			}
			totalScore += Math.log(count + 1 + 0.001);
		}
		return totalScore;
	}
}
