package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.benchmark.Events;
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
			
			TypeDistribution domains = statistics.domainsOf(property);
			TypeDistribution domainSummaries = this.domainSummaries.of(property);
			
			TypeDistribution ranges = statistics.rangesOf(property);
			TypeDistribution rangeSummaries = this.rangeSummaries.of(property);
			
			track("domains", property, domains);
			track("ranges", property, ranges);
			
			double domainSimilarity = similarity.between(domains, domainSummaries);
			double rangeSimilarity = similarity.between(ranges, rangeSummaries);
			
			CandidateProperty resource = new CandidateProperty(property);
			resource.sumScore(domainSimilarity);
			resource.sumScore(rangeSimilarity);
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}

	private void track(String what, String property, TypeDistribution domains) {
		Events.simple().debug(property + " " + what);
		for(String domain : domains.all()){
			Events.simple().debug(domain + "|" + domains.typeOccurrence(domain) + "|" + domains.propertyOccurrence() + "|" + domains.propertyOccurrenceForType(domain));
		}
	}
}


