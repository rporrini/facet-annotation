package it.disco.unimib.labeller.index;

import java.util.List;

public interface ReadAndWriteStore extends WriteStore{
	
	public List<CandidateProperty> get(String entity) throws Exception;
}