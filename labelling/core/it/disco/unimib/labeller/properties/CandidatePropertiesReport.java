package it.disco.unimib.labeller.properties;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.index.ContextualizedValues;
import it.disco.unimib.labeller.index.SelectionCriterion;

import org.apache.commons.lang3.StringUtils;

public class CandidatePropertiesReport implements Properties{

	private Properties properties;

	public CandidatePropertiesReport(Properties properties){
		this.properties = properties;
	}
	
	@Override
	public PropertyDistribution forValues(ContextualizedValues request, SelectionCriterion query) throws Exception {
		log("processing " + request.domain() + " [" + StringUtils.join(request.all(), ", ") + "]");
		
		PropertyDistribution distribution = properties.forValues(request, query);
		String header = "|";
		for(String value : distribution.values()){
			header += value + "|";
		}
		log(header);
		for(String property : distribution.properties()){
			String line = property + "|";
			for(String value : distribution.values()){
				line += distribution.scoreOf(property, value) + "|";
			}
			log(line);
		}
		return distribution;
	}

	private void log(String header) {
		Events.simple().debug(">" + header);
	}
}