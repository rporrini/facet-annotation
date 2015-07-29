package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.InputFile;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

public class TypeDistributions {

	HashMap<String, TypeDistribution> distributions;
	
	public TypeDistributions(InputFile... files) throws Exception {
		this.distributions = extractDistributionsFrom(files);
	}

	private HashMap<String, TypeDistribution> extractDistributionsFrom(InputFile... files) throws Exception {
		HashMap<String, TypeDistribution> result = new HashMap<String, TypeDistribution>();
		for(InputFile file : files){
			TypeDistribution distribution = new TypeDistribution();
			for(String line : file.lines()){
				distribution.track(StringUtils.split(line, "|"));
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
