package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.benchmark.Events;

import java.util.ArrayList;
import java.util.Collection;

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
		for(CandidateResource property : candidates.asList()){
			Events.simple().debug(property.uri() + " - " + property.score());
			Events.simple().debug(filter(property.domains()));
			Events.simple().debug(filter(property.ranges()));
			Events.simple().debug("-------------");
		}
		return candidates;
	}

	private String filter(Collection<CandidateResource> subjectTypes) {
		ArrayList<String> filtered = new ArrayList<String>();
		for(CandidateResource type : subjectTypes){
			if(!type.uri().contains("/resource/Category:")) filtered.add(type.toString());
		}
		return StringUtils.join(filtered, ", ");
	}
	
}