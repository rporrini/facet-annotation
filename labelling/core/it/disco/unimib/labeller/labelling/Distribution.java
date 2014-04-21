package it.disco.unimib.labeller.labelling;

import it.disco.unimib.labeller.index.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Distribution{
	private HashMap<String, HashMap<String, List<SearchResult>>> predicatesDistribution;
	
	public Distribution(HashMap<String, List<SearchResult>> valueDistribution) {
		this.predicatesDistribution = invert(valueDistribution);
	}
	
	public double scoreOf(String predicate, String value) {
		List<SearchResult> list = this.predicatesDistribution.get(predicate).get(value);
		double count = 0.0;
		if(list != null){
			for(SearchResult result : list){
				count += result.score();
			}
		}
		return count;
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
}