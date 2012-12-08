package edu.cmu.cs.cs214.hw9.backend;

import java.util.*;

public class Cache {
	private static final int CACHE_SIZE = 5;
	private Queue<String> cacheOrderList = new LinkedList<String>();
	private Map<String, String> cache = new HashMap<String, String>();
	
	/**
	 * Checks if the request is cached
	 * @param request
	 * @return Returns true if the response to this request is cached
	 */
	public boolean requestInCache(String request) {
		return cacheOrderList.contains(request);
	}
	
	/**
	 * Insert the request/response pair into the cache
	 * @param request The request to cache
	 * @param response The response to cache
	 */
	public void cacheRequest(String request, String response) {
		if (cacheOrderList.size() == CACHE_SIZE) {
			//cache is full, remove the oldest
			String key = cacheOrderList.remove();
			cache.remove(key);
		}
		
		cacheOrderList.add(request);
		cache.put(request, response);
	}
	
	/**
	 * Fetches the response for the given request from the cache.
	 * NOTE: there is a precondition that the request IS in the cache.
	 * 
	 * @param request A request that has a cached response
	 * @return Returns the response for the given request
	 */
	public String getResponseForRequest(String request) {
		//update the the list of most recently used.
		cacheOrderList.remove(request);
		cacheOrderList.add(request);
		
		return cache.get(request);
	}
	

}
