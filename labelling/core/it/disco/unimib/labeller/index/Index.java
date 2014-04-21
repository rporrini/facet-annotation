package it.disco.unimib.labeller.index;

import java.util.List;

public interface Index {

	public List<SearchResult> get(String type, String context) throws Exception;

}