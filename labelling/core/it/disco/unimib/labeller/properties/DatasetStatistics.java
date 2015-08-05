package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateProperty;
import it.disco.unimib.labeller.index.CandidateResources;

import java.util.HashMap;
import java.util.Set;

public class DatasetStatistics{
	
	private HashMap<String, CandidateResources> propertiesForValues;
	private TypeDistribution overallTypeDistribution;

	public DatasetStatistics(HashMap<String, CandidateResources> results){
		this.propertiesForValues = results;
		this.overallTypeDistribution = extractOverallTypeDistribution(results);
	}
	
	public TypeDistribution rangesOf(String propertyUri){
		return getDomainsOrRanges(propertyUri, false);
	}
	
	public TypeDistribution domainsOf(String propertyUri) {
		return getDomainsOrRanges(propertyUri, true);
	}
	
	private Set<String> values(){
		return propertiesForValues.keySet();
	}

	private TypeDistribution getDomainsOrRanges(String propertyUri, boolean domains) {
		TypeDistribution distribution = new TypeDistribution();
		double occurrencesOfTheProperty = 0.0;
		for(String value : values()){
			CandidateProperty property = getOrDefault(propertyUri, value);
			TypeDistribution types = null;
			if(domains) {
				types = property.domains();
			}else{
				types = property.ranges();
			}
			occurrencesOfTheProperty += (double)property.totalOccurrences();
			for(String range : types.all()){
				distribution.trackPropertyOccurrenceForType(range, types.typeOccurrence(range) + "");
			}
		}
		distribution.trackPropertyOccurrence(occurrencesOfTheProperty + "");
		for(String type : distribution.all()){
			distribution.trackTypeOccurrence(type, overallTypeDistribution.typeOccurrence(type) + "");
		}
		return distribution;
	}
	
	private TypeDistribution extractOverallTypeDistribution(HashMap<String, CandidateResources> results) {
		TypeDistribution distribution = new TypeDistribution();
		for(String value : results.keySet()){
			for(CandidateProperty property : results.get(value).asList()){
				TypeDistribution domains = property.domains();
				for(String domain : domains.all()){
					distribution.trackTypeOccurrence(domain, domains.typeOccurrence(domain) + "");
				}
				TypeDistribution ranges = property.ranges();
				for(String range : ranges.all()){
					distribution.trackTypeOccurrence(range, ranges.typeOccurrence(range) + "");
				}
			}
		}
		return distribution;
	}
	
	private CandidateProperty getOrDefault(String property, String value) {
		return propertiesForValues.get(value).get(new CandidateProperty(property));
	}
}