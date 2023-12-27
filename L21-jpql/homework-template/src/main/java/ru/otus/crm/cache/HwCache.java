package ru.otus.crm.cache;


public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    boolean exists(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);
}
