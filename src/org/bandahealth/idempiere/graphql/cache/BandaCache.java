package org.bandahealth.idempiere.graphql.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.dataloader.CacheMap;

import java.util.concurrent.TimeUnit;

public class BandaCache<U, V> implements CacheMap<U, V> {

	private final int CACHE_TIMEOUT_IN_MINUTES = 5;
	private final int CACHE_MAX_SIZE = 200;
	private final Cache<U, V> cache;

	public BandaCache() {
		cache = Caffeine.newBuilder().expireAfterWrite(CACHE_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES)
				.maximumSize(CACHE_MAX_SIZE).build();
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
