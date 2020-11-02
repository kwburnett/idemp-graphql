package org.bandahealth.idempiere.graphql.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.dataloader.CacheMap;

import java.util.concurrent.TimeUnit;

/**
 * This is a wrapper for the Caffeine cache (or whichever is used) that implements the GraphQL Java interface. This
 * allows us to cache whatever we'd like
 *
 * @param <U> The key type of the cache.
 * @param <V> The value type of entities stored in the cache.
 */
public class BandaCache<U, V> implements CacheMap<U, V> {
	private final Cache<U, V> cache;

	public BandaCache(Cache<U, V> cache) {
		this.cache = cache;
	}

	public Cache<U, V> getCache() {
		return cache;
	}

	@Override
	public boolean containsKey(U key) {
		return cache.getIfPresent(key) != null;
	}

	@Override
	public V get(U key) {
		return cache.getIfPresent(key);
	}

	@Override
	public CacheMap<U, V> set(U key, V value) {
		cache.put(key, value);
		return this;
	}

	@Override
	public CacheMap<U, V> delete(U key) {
		cache.invalidate(key);
		return this;
	}

	@Override
	public CacheMap<U, V> clear() {
		cache.invalidateAll();
		return this;
	}
}
