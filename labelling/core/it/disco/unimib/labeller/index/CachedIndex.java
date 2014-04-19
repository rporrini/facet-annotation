package it.disco.unimib.labeller.index;

import java.util.HashMap;
import java.util.List;

public class CachedIndex implements Index {

	private HashMap<String, List<String>> cache;
	private Index index;

	public CachedIndex(Index indexToCache){
		this.index = indexToCache;
		this.cache = new HashMap<String, List<String>>();
	}
	
	@Override
	public List<String> get(String type, String context) throws Exception {
		String cacheKey = type + context;
		List<String> cachedResult = cache.get(cacheKey);
		if(cachedResult == null){
			List<String> retrievedResult = index.get(type, context);
			cache.put(cacheKey, retrievedResult);
			cachedResult = retrievedResult;
		}
		return cachedResult;
	}
}
