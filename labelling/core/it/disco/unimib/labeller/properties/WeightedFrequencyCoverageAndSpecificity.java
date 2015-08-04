package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;
import it.disco.unimib.labeller.index.TypeConsistency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	public List<CandidateProperty> annotate(ContextualizedValues request) throws Exception {
		
		PropertyDistribution distribution = new CandidateProperties(index).forValues(request, selection);
		
		ArrayList<CandidateProperty> results = new ArrayList<CandidateProperty>();
		for(String property : distribution.properties()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(property, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
			
			double rangeDisc = rangeDiscriminancy(property, distribution);
			double propertyDisc = propertyDiscriminancy(new ContextualizedValues(request.domain(), new String[]{property}), distribution.asStatistics().domainsOf(property));
			double smoothedWFreq = smoothedWeightedFrequency(frequencyOverValues, distribution);
			double coverage = coverage(covered, distribution);
			
			CandidateProperty resource = new CandidateProperty(property);
			
			resource.multiplyScore(smoothedWFreq);
			resource.multiplyScore(coverage);
			resource.multiplyScore(propertyDisc);
			resource.multiplyScore(rangeDisc);
			
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}

	private double coverage(double covered, PropertyDistribution distribution) {
		return covered / (double)distribution.values().size();
	}

	private double smoothedWeightedFrequency(double frequencyOverValues, PropertyDistribution distribution) {
		return Math.log(frequencyOverValues / (double)distribution.values().size() + 1.000000001);
	}

	private double rangeDiscriminancy(String property, PropertyDistribution distribution) {
		return 1.0 + Math.log((this.consistency.consistencyOf(distribution.asStatistics().rangesOf(property)) / (double)distribution.values().size()) + 1.0);
	}

	private double propertyDiscriminancy(ContextualizedValues specificity, TypeDistribution subjects) throws Exception {
		specificity.setDomains(subjects.all().toArray(new String[subjects.size()]));
		return Math.log(this.propertySpecificity.of(specificity) + 1.1);
	}
}


