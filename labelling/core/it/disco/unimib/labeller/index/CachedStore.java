package it.disco.unimib.labeller.index;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CachedStore implements ReadAndWriteStore{

	private ReadAndWriteStore store;
	private Map<String, List<CandidateProperty>> cache;
	private int maxSize;

	public CachedStore(ReadAndWriteStore store, int maxSize) {
		this.store = store;
		this.cache = Collections.synchronizedMap(new LinkedHashMap<String, List<CandidateProperty>>());
		this.maxSize = maxSize;
	}
	
	@Override
	public WriteStore add(NTriple triple) throws Exception {
		return store.add(triple);
	}

	@Override
	public synchronized List<CandidateProperty> get(String entity) throws Exception {
		List<CandidateProperty> cachedResults = cache.get(entity);
		if(cachedResults != null) return cachedResults;
		
		List<CandidateProperty> results = store.get(entity);
		if(cache.size() >= maxSize){
			cache.remove(cache.keySet().iterator().next());
		}
		cache.put(entity, results);
		return results;
	}
}