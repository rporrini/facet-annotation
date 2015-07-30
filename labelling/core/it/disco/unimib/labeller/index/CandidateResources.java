package it.disco.unimib.labeller.index;

import java.util.Collection;
import java.util.HashMap;

public class CandidateResources {

	HashMap<String, CandidateResource> resources;
	
	public CandidateResources(){
		resources = new HashMap<String, CandidateResource>();
	}
	
	public CandidateResource get(CandidateResource resource) {
		String id = resource.uri();
		if(!resources.containsKey(id)) resources.put(resource.uri(), resource);
		return resources.get(resource.uri());
	}

	public Collection<CandidateResource> asList() {
		return resources.values();
	}
}
