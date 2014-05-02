package it.disco.unimib.labeller.index;

import it.disco.unimib.labeller.labelling.Events;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CachedIndex implements Index {

	private Index indexToCache;
	private ConcurrentHashMap<String, List<AnnotationResult>> cache;
	private long hits;
	private long miss;
	private long totalResources;
	private String name;

	public CachedIndex(Index indexToCache, String name){
		this.indexToCache = indexToCache;
		this.cache = new ConcurrentHashMap<String, List<AnnotationResult>>();
		this.hits = 0;
		this.miss = 0;
		this.totalResources = 0;
		this.name = name;
	}
	
	@Override
	public List<AnnotationResult> get(String type, String context) throws Exception {
		String key = type+context;
		List<AnnotationResult> result = cache.get(key);
		hits++;
		if(result == null){
			result = indexToCache.get(type, context);
			cache.put(key, result);
			miss++;
			hits--;
		}
		totalResources = totalResources + result.size();
		if((hits + miss) % 1000000 == 0){
			new Events().debug("[" + name + "] hits: " + hits + " miss: " + miss + " ratio: " + (double)hits/((double)miss + (double)hits));
			new Events().debug("[" + name + "] mean results: " + (double)totalResources/(double)(miss+hits));
		}
		return result;
	}

}
