package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.ScaledDepth;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WeightedFrequencyCoverageAndSpecificity implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity predicateSpecificity;
	private TripleSelectionCriterion selection;
	private ScaledDepth depth;

	public WeightedFrequencyCoverageAndSpecificity(ScaledDepth depth, Index index, TripleSelectionCriterion criterion, Specificity predicateDiscriminacy) {
		this.index = index;
		this.selection = criterion;
		this.predicateSpecificity = predicateDiscriminacy;
		this.depth = depth;
	}

	@Override
	public List<CandidateResource> typeOf(String domain, List<String> elements) throws Exception {
		
		Distribution distribution = new CandidatePredicates(index).forValues(domain, elements.toArray(new String[elements.size()]), selection);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String predicate : distribution.predicates()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(predicate, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
			
			Map<String, Double> objectsOf = distribution.objectsOf(predicate);
			double tot=0;
			for(String type : objectsOf.keySet()){
				tot += (objectsOf.get(type) * this.depth.of(type));
			}
			if(!objectsOf.isEmpty()) tot = tot / (double)objectsOf.size();
			
			new Events().debug("distinct object types: " + objectsOf.size());
			
			double objectDisc = 1.0 + Math.log(tot + 1.0);
			double disc = Math.log(predicateSpecificity.of(predicate, domain) + 1.1);
			double smoothedWFreq = Math.log((frequencyOverValues / (double)distribution.values().size()) + 1.000000001);
			double coverage = covered / (double)distribution.values().size();
			
			CandidateResource resource = new CandidateResource(predicate);
			
			resource.multiplyScore(smoothedWFreq);
			resource.multiplyScore(coverage);
			resource.multiplyScore(disc);
			resource.multiplyScore(objectDisc);
			
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}
}


