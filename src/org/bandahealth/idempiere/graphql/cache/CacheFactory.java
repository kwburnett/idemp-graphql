package org.bandahealth.idempiere.graphql.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.compiere.model.MRefList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This factory helps construct caches for the specified classes. Typically, these classes will be iDempiere entities
 * so GraphQL doesn't have to go to the DB to fetch data.
 */
public class CacheFactory {
	private final int DEFAULT_CACHE_TIMEOUT_IN_MINUTES = 5;
	private final int DEFAULT_CACHE_MAX_SIZE = 200;
	private final Map<String, BandaCache<Object, Object>> cacheMap = new HashMap<>();

	public BandaCache<Object, Object> getCache(Class<?> clazz) {
		BandaCache<Object, Object> classCache = cacheMap.get(clazz.getName());
		if (classCache == null) {
			classCache = createCache(clazz);
			cacheMap.put(clazz.getName(), classCache);
		}
		return classCache;
	}

	/**
	 * Specify different types of caches for different types of objects (i.e. to persist objects different ways)
	 *
	 * @param clazz The class the cache is for, typically an iDempiere entity
	 * @return A cache ready to use for the specified object
	 */
	private BandaCache<Object, Object> createCache(Class<?> clazz) {
		// Create a default cache
		Cache<Object, Object> cacheToUse = Caffeine.newBuilder()
				.expireAfterWrite(DEFAULT_CACHE_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES).maximumSize(DEFAULT_CACHE_MAX_SIZE)
				.build();

		if (clazz.getName().equalsIgnoreCase(MRefList.class.getName())) {
			cacheToUse = Caffeine.newBuilder().expireAfterWrite(Long.MAX_VALUE, TimeUnit.DAYS)
					.maximumSize(DEFAULT_CACHE_MAX_SIZE).build();
		}
		return new BandaCache<>(cacheToUse);
	}
}
