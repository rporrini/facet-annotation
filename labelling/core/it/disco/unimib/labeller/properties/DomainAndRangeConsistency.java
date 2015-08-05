package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DomainAndRangeConsistency implements AnnotationAlgorithm{
	
	private Index index;
	private SelectionCriterion selection;
	
	private DatasetSummary domainSummaries;
	private DatasetSummary rangeSummaries;

	public DomainAndRangeConsistency(Index index, SelectionCriterion criterion, DatasetSummary domainSummaries, DatasetSummary rangeSummaries) {
		this.index = index;
		this.selection = criterion;
		this.domainSummaries = domainSummaries;
		this.rangeSummaries = rangeSummaries;
	}

	@Override
	public List<CandidateProperty> annotate(ContextualizedValues request) throws Exception {
		
		PropertyDistribution distribution = new CandidateProperties(index).forValues(request, selection);
		DatasetStatistics statistics = distribution.asStatistics();
		
		ArrayList<CandidateProperty> results = new ArrayList<CandidateProperty>();
		for(String property : distribution.properties()){
			CosineSimilarity similarity = new CosineSimilarity();
			
			double domainSimilarity = similarity.between(statistics.domainsOf(property), domainSummaries.of(property));
			double rangeSimilarity = similarity.between(statistics.rangesOf(property), rangeSummaries.of(property));
			
			CandidateProperty resource = new CandidateProperty(property);
			resource.multiplyScore(domainSimilarity);
			resource.multiplyScore(rangeSimilarity);
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}
}


