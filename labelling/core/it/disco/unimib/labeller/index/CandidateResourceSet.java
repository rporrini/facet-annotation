package it.disco.unimib.labeller.index;

import java.util.Collection;
import java.util.HashMap;

public class CandidateResourceSet {

	HashMap<String, CandidateResource> resources;
	
	public CandidateResourceSet(){
		resources = new HashMap<String, CandidateResource>();
	}
	
	public CandidateResource get(CandidateResource resource) {
		if(!resources.containsKey(resource.id())) resources.put(resource.id(), resource);
		return resources.get(resource.id());
	}

	public Collection<CandidateResource> asList() {
		return resources.values();
	}
}
