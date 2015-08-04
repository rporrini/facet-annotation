package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.CandidateResource;
import it.disco.unimib.labeller.index.CandidateResources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class DatasetStatistics{
	
	private HashMap<String, CandidateResources> propertiesForValues;

	public DatasetStatistics(HashMap<String, CandidateResources> results){
		this.propertiesForValues = results;
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
		return distribution;
	}
	
	private CandidateResource getOrDefault(String property, String value) {
		return propertiesForValues.get(value).get(new CandidateResource(property));
	}
}