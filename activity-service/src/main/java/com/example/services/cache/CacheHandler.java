package com.example.services.cache;

import java.util.concurrent.Future;

public interface CacheHandler<K, V> {

    V get(K key);

    Future<V> getAsync(K key);

    void put(K key, V value);

    void putAsync(K key, V value);
}
