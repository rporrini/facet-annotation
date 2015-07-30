package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
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
	public List<CandidateResource> annotate(ContextualizedValues request) throws Exception {
		
		PropertyDistribution distribution = new CandidateProperties(index).forValues(request, selection);
		
		ArrayList<CandidateResource> results = new ArrayList<CandidateResource>();
		for(String property : distribution.properties()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(property, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
			
			double rangeDisc = rangeDiscriminancy(property, distribution);
			double domainDisc = domainDiscriminacy(new ContextualizedValues(request.domain(), new String[]{property}), distribution.domainsOf(property));
			double smoothedWFreq = smoothedWeightedFrequency(frequencyOverValues, distribution);
			double coverage = coverage(distribution, covered);
			
			CandidateResource resource = new CandidateResource(property);
			
			resource.multiplyScore(smoothedWFreq);
			resource.multiplyScore(coverage);
			resource.multiplyScore(domainDisc);
			resource.multiplyScore(rangeDisc);
			
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}

	private double coverage(PropertyDistribution distribution, double covered) {
		return covered / (double)distribution.values().size();
	}

	private double smoothedWeightedFrequency(double frequencyOverValues, PropertyDistribution distribution) {
		return Math.log(coverage(distribution, frequencyOverValues) + 1.000000001);
	}

	private double rangeDiscriminancy(String property,
			PropertyDistribution distribution) {
		return 1.0 + Math.log(this.consistency.consistencyOf(distribution.rangesOf(property)) + 1.0);
	}

	private double domainDiscriminacy(ContextualizedValues specificity, TypeDistribution subjectsOf) throws Exception {
		specificity.setDomains(subjectsOf.all().toArray(new String[subjectsOf.size()]));
		return Math.log(propertySpecificity.of(specificity) + 1.1);
	}
}


