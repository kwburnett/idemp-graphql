package org.bandahealth.idempiere.graphql.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.compiere.model.MRefList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CacheFactory {
	private final int CACHE_TIMEOUT_IN_MINUTES = 5;
	private final int CACHE_MAX_SIZE = 200;
	private final Map<String, BandaCache<Object, Object>> cacheMap = new HashMap<>();

	public BandaCache<Object, Object> getCache(Class<?> clazz) {
		BandaCache<Object, Object> classCache = cacheMap.get(clazz.getName());
		if (classCache == null) {
			classCache = createCache(clazz);
			cacheMap.put(clazz.getName(), classCache);
		}
		return classCache;
	}

	private BandaCache<Object, Object> createCache(Class<?> clazz) {
		// Create a default cache
		Cache<Object, Object> cacheToUse = Caffeine.newBuilder()
				.expireAfterWrite(CACHE_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES).maximumSize(CACHE_MAX_SIZE).build();;
		if (clazz.getName().equalsIgnoreCase(MRefList.class.getName())) {
			cacheToUse = Caffeine.newBuilder().expireAfterWrite(Long.MAX_VALUE, TimeUnit.DAYS)
					.maximumSize(CACHE_MAX_SIZE).build();
		}
		return new BandaCache<>(cacheToUse);
	}
}
