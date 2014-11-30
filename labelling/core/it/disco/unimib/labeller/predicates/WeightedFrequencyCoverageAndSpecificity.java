package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.AllValues;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.NoContext;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WeightedFrequencyCoverageAndSpecificity implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity predicateSpecificity;
	private TripleSelectionCriterion selection;

	public WeightedFrequencyCoverageAndSpecificity(Index index, TripleSelectionCriterion criterion, Specificity predicateDiscriminacy) {
		this.index = index;
		this.selection = criterion;
		this.predicateSpecificity = predicateDiscriminacy;
	}

	@Override
	public List<CandidateResource> typeOf(String domain, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicatesReport(new CandidatePredicates(index))
										.forValues(domain, elements.toArray(new String[elements.size()]), selection);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String predicate : distribution.predicates()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(predicate, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
				
				new Events().debug(predicate + " - " + value + " SUBJECTS: " + getTopOccurrent(sortByValue(distribution.subjectsOf(predicate, value))));
				new Events().debug(predicate + " - " + value + " OBJECTS: " + getTopOccurrent(sortByValue(distribution.objectsOf(predicate, value))));
			}
			
			CandidateResource resource = new CandidateResource(predicate);
			
			long overallFrequency = index.countPredicatesInContext(predicate, domain, new NoContext(new AllValues()));
			double disc = predicateSpecificity.of(predicate, domain, overallFrequency);
			double smoothedWFreq = Math.log((frequencyOverValues / (double)distribution.values().size()) + 1.000000001);
			double coverage = covered / (double)distribution.values().size();
			
			resource.multiplyScore(smoothedWFreq);
			resource.multiplyScore(coverage);
			resource.multiplyScore(disc);
			
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}
	
	private Map<String, Double>  sortByValue(Map<String, Double> map )
	{
	    List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
	    Collections.sort(list, new Comparator<Map.Entry<String, Double>>()
	    {
	        @Override
	        public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
	        {
	            return (o2.getValue()).compareTo(o1.getValue());
	        }
	    } );
	
	    Map<String, Double> result = new LinkedHashMap<>();
	    for (Map.Entry<String, Double> entry : list)
	    {
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	
	private Map<String, Double> getTopOccurrent(Map<String, Double> input){
		HashMap<String, Double> hashMap = new HashMap<String, Double>();
		int topK = 0;
		for(String key: input.keySet()){
			if(topK < 10){
				hashMap.put(key, input.get(key));
			}
			topK++;
		}
		return sortByValue(hashMap);
	}
}


