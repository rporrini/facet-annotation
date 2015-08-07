package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.index.InputFile;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
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
			HashSet<Double> propertyFrequencies = new HashSet<Double>();
			LineIterator lines = IOUtils.lineIterator(file.content(), "UTF-8");
			while(lines.hasNext()){
				String line = lines.nextLine();
				String[] splitted = StringUtils.split(line, "|");
				
				String type = splitted[0];
				
				if(distribution.typeOccurrence(type) == 0){
					distribution.trackTypeOccurrence(type, splitted[1]);
				}
				
				double propertyOccurrence = Double.parseDouble(splitted[2]);
				propertyFrequencies.add(propertyOccurrence);
				
				distribution.trackPropertyOccurrenceForType(type, splitted[3]);
			}
			lines.close();
			double propertyOccurrence = 0.0;
			for(Double frequency : propertyFrequencies){
				propertyOccurrence+=frequency;
			}
			distribution.trackPropertyOccurrence(propertyOccurrence + "");
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
