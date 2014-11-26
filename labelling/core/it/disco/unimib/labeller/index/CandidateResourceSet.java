package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CandidateResourceSet {

	HashMap<String, CandidateResource> resources;
	
	public CandidateResourceSet(){
		resources = new HashMap<String, CandidateResource>();
	}
	
	public CandidateResource get(CandidateResource resource) {
		if(!resources.containsKey(resource.id())) resources.put(resource.id(), resource);
		return resources.get(resource.id());
	}

	public List<CandidateResource> asList() {
		return new ArrayList<CandidateResource>(resources.values());
	}
}
