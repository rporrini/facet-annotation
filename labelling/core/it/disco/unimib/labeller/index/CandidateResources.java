package it.disco.unimib.labeller.index;

import java.util.Collection;
import java.util.HashMap;

public class CandidateResources {

	HashMap<String, CandidateProperty> resources;
	
	public CandidateResources(){
		resources = new HashMap<String, CandidateProperty>();
	}
	
	public CandidateProperty get(CandidateProperty resource) {
		String id = resource.uri();
		if(!resources.containsKey(id)) resources.put(resource.uri(), resource);
		return resources.get(resource.uri());
	}

	public Collection<CandidateProperty> asList() {
		return resources.values();
	}
}
