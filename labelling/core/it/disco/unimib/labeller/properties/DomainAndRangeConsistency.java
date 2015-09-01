package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.Index;
import it.disco.unimib.labeller.index.ScaledDepths;
import it.disco.unimib.labeller.index.SelectionCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DomainAndRangeConsistency implements AnnotationAlgorithm{
	
	private Index index;
	private SelectionCriterion selection;
	
	private DatasetSummary domainSummaries;
	private DatasetSummary rangeSummaries;
	
	private ScaledDepths depth;

	public DomainAndRangeConsistency(Index index, SelectionCriterion criterion, DatasetSummary domainSummaries, DatasetSummary rangeSummaries, ScaledDepths depth) {
		this.index = index;
		this.selection = criterion;
		this.domainSummaries = domainSummaries;
		this.rangeSummaries = rangeSummaries;
		this.depth = depth;
	}

	@Override
	public List<CandidateProperty> annotate(ContextualizedValues request) throws Exception {
		
		PropertyDistribution distribution = new CandidateProperties(index).forValues(request, selection);
		DatasetStatistics statistics = distribution.asStatistics();
		
		ArrayList<CandidateProperty> results = new ArrayList<CandidateProperty>();
		for(String property : distribution.properties()){
			double frequencyOverValues = 0;
			double covered = 0;
			for(String value : distribution.values()){
				double score = distribution.scoreOf(property, value);
				if(score > 0) covered++;
				frequencyOverValues += score;
			}
			
			DepthJaccardSimilarity similarity = new DepthJaccardSimilarity(this.depth);
			
			TypeDistribution domains = statistics.domainsOf(property);
			TypeDistribution domainSummaries = this.domainSummaries.of(property);
			
			TypeDistribution ranges = statistics.rangesOf(property);
			TypeDistribution rangeSummaries = this.rangeSummaries.of(property);
			
			track("dataset domains", property, domains);
			track("summary domains", property, domainSummaries);
			track("dataset ranges", property, ranges);
			track("summary ranges", property, rangeSummaries);
			
			double domainSimilarity = 1.0 + Math.log(similarity.between(domains, domainSummaries) + 1.000000001);
			double rangeSimilarity = 1.0 + Math.log(similarity.between(ranges, rangeSummaries) + 1.000000001);
			double smoothedWFreq = 1.0 + Math.log(frequencyOverValues / (double)distribution.values().size() + 1.000000001);
			double coverage = covered / (double)distribution.values().size();
			
			CandidateProperty resource = new CandidateProperty(property);
			resource.multiplyScore(domainSimilarity);
			resource.multiplyScore(rangeSimilarity);
			resource.multiplyScore(smoothedWFreq);
			resource.multiplyScore(coverage);
			
			results.add(resource);
		}
		
		Collections.sort(results);
		return results;
	}

	private void track(String what, String property, TypeDistribution domains) {
		Events.simple().debug(property + " -- " + what);
		for(String domain : domains.all()){
			Events.simple().debug(domain + "|" + domains.typeOccurrence(domain) + "|" + domains.propertyOccurrence() + "|" + domains.propertyOccurrenceForType(domain));
		}
	}
}


