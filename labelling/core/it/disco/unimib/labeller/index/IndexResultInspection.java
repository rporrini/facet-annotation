package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.Events;
import it.disco.unimib.labeller.properties.TypeDistribution;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class IndexResultInspection implements Index{

	private Index index;

	public IndexResultInspection(Index indexToInspect) {
		this.index = indexToInspect;
	}
	
	@Override
	public long count(Constraint query) throws Exception {
		return index.count(query);
	}

	@Override
	public CandidateResources get(ContextualizedValues request, Constraint query) throws Exception {
		CandidateResources candidates = index.get(request, query);
		Events.simple().debug("domain: " + request.domain() + " - value: " + request.first());
		for(CandidateProperty property : candidates.asList()){
			Events.simple().debug(property.uri() + " - " + property.score());
			Events.simple().debug(filter(property.domains()));
			Events.simple().debug(filter(property.ranges()));
			Events.simple().debug("-------------");
		}
		return candidates;
	}

	private String filter(TypeDistribution subjectTypes) {
		ArrayList<String> filtered = new ArrayList<String>();
		for(String type : subjectTypes.all()){
			if(!type.contains("/resource/Category:")) filtered.add(type);
		}
		return StringUtils.join(filtered, ", ");
	}
	
}