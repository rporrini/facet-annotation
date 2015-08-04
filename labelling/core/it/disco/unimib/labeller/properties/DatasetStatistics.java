package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResources;

import java.util.Collection;
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
			CandidateResource property = getOrDefault(propertyUri, value);
			Collection<CandidateResource> types = null;
			if(domains) {
				types = property.domains();
			}else{
				types = property.ranges();
			}
			for(CandidateResource range : types){
				occurrencesOfTheProperty += (double)property.totalOccurrences();
				distribution.trackPropertyOccurrenceForType(range.uri(), range.score() + "");
			}
		}
		distribution.trackPropertyOccurrence(occurrencesOfTheProperty + "");
		for(String type : overallTypeDistribution.all()){
			distribution.trackTypeOccurrence(type, overallTypeDistribution.typeOccurrence(type) + "");
		}
		return distribution;
	}
	
	private TypeDistribution extractOverallTypeDistribution(HashMap<String, CandidateResources> results) {
		TypeDistribution distribution = new TypeDistribution();
		for(String value : results.keySet()){
			for(CandidateResource property : results.get(value).asList()){
				for(CandidateResource domain : property.domains()){
					distribution.trackTypeOccurrence(domain.uri(), domain.score() + "");
				}
				for(CandidateResource range : property.ranges()){
					distribution.trackTypeOccurrence(range.uri(), range.score() + "");
				}
			}
		}
		return distribution;
	}
	
	private CandidateResource getOrDefault(String property, String value) {
		return propertiesForValues.get(value).get(new CandidateResource(property));
	}
}