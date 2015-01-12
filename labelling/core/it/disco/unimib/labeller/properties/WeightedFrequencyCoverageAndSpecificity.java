package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.TypeConsistency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WeightedFrequencyCoverageAndSpecificity implements AnnotationAlgorithm{
	
	private Index index;
	private Specificity propertySpecificity;
	private SelectionCriterion selection;
	private TypeConsistency consistency;

	public WeightedFrequencyCoverageAndSpecificity(TypeConsistency consistency, Index index, SelectionCriterion criterion, Specificity propertySpecificity) {
		this.index = index;
		this.selection = criterion;
		this.propertySpecificity = propertySpecificity;
		this.consistency = consistency;
	}

	@Override
	public List<CandidateResource> typeOf(ContextualizedValues request) throws Exception {
		
		Distribution distribution = new CandidateProperties(index).forValues(request, selection);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String property : distribution.properties()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(property, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
			Map<String, Double> objects = distribution.objectsOf(property);
			new Events().debug("distinct object types: " + objects.size() + "|" + property);
			
			double objectDisc = 1.0 + Math.log(this.consistency.consistencyOf(objects) + 1.0);
			ContextualizedValues typedProperties = new ContextualizedValues(request.domain(), new String[]{property});
			double disc = Math.log(propertySpecificity.of(typedProperties) + 1.1);
			double smoothedWFreq = Math.log((frequencyOverValues / (double)distribution.values().size()) + 1.000000001);
			double coverage = covered / (double)distribution.values().size();
			
			CandidateResource resource = new CandidateResource(property);
			
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


