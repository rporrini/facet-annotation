package it.disco.unimib.labeller.predicates;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.TripleSelectionCriterion;
import it.disco.unimib.labeller.index.TypeConsistency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WeightedFrequencyCoverageAndSpecificity implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity predicateSpecificity;
	private TripleSelectionCriterion selection;
	private TypeConsistency consistency;

	public WeightedFrequencyCoverageAndSpecificity(TypeConsistency consistency, Index index, TripleSelectionCriterion criterion, Specificity predicateDiscriminacy) {
		this.index = index;
		this.selection = criterion;
		this.predicateSpecificity = predicateDiscriminacy;
		this.consistency = consistency;
	}

	@Override
	public List<CandidateResource> typeOf(AnnotationRequest request) throws Exception {
		
		Distribution distribution = new CandidatePredicates(index).forValues(request.context(), request.elements(), selection);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String predicate : distribution.predicates()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(predicate, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
			Map<String, Double> objects = distribution.objectsOf(predicate);
			new Events().debug("distinct object types: " + objects.size() + "|" + predicate);
			
			double objectDisc = 1.0 + Math.log(this.consistency.consistencyOf(objects) + 1.0);
			double disc = Math.log(predicateSpecificity.of(predicate, request.context()) + 1.1);
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


