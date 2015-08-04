package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.InputFile;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class DatasetSummary {

	HashMap<String, TypeDistribution> distributions;
	
	public DatasetSummary(InputFile... files) throws Exception {
		this.distributions = extractDistributionsFrom(files);
	}

	private HashMap<String, TypeDistribution> extractDistributionsFrom(InputFile... files) throws Exception {
		HashMap<String, TypeDistribution> result = new HashMap<String, TypeDistribution>();
		for(InputFile file : files){
			TypeDistribution distribution = new TypeDistribution();
			for(String line : file.lines()){
				String[] splitted = StringUtils.split(line, "|");
				
				String type = splitted[0];
				
				distribution.trackTypeOccurrence(type, splitted[1]);
				distribution.trackPropertyOccurrence(splitted[2]);
				distribution.trackPropertyOccurrenceForType(type, splitted[3]);
			}
			result.put(file.name(), distribution);
		}
		return result;
	}

	public TypeDistribution of(String property) {
		String propertyId = property.replace("http://", "").replace("/", "_");
		TypeDistribution distribution = distributions.get(propertyId);
		if(distribution == null) distribution = new TypeDistribution();
		return distribution;
	}
}
