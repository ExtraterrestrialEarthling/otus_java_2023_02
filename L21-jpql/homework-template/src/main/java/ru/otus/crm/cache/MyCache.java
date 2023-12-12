package ru.otus.crm.cache;


import java.lang.ref.SoftReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private final int capacity;
    private final Map<K, SoftReference<V>> map;
    private final List<HwListener<K, V>> listeners;

    public MyCache(int capacity) {
        listeners = new ArrayList<>();
        this.capacity = capacity;
        map = new LinkedHashMap<>(capacity);
    }

    @Override
    public boolean exists(K key) {
        SoftReference<V> softValue = map.get(key);
        return softValue != null && softValue.get() != null;
    }

    @Override
    public void put(K key, V value) {
        if (exists(key)) {
            map.put(key, new SoftReference<V>(value));
        } else {
            if (map.size() == capacity) {
                map.remove(map.keySet().iterator().next());
            }
            map.put(key, new SoftReference<V>(value));
            notifyListenersOnPut(key, value);
        }
    }

    @Override
    public void remove(K key) {
        map.remove(key);
        notifyListenersOnRemove(key);
    }

    @Override
    public V get(K key) {
        SoftReference<V> softValue = map.remove(key);
        V value = softValue == null ? null : softValue.get();
        if (value == null) {
            throw new NoSuchElementException();
        }
        map.put(key, softValue);
        notifyListenersOnGet(key, value);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListenersOnPut(K key, V value) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, "put to cache");
        }
    }

    private void notifyListenersOnRemove(K key) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, null, "removed from cache");
        }
    }

    private void notifyListenersOnGet(K key, V value) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, "retrieved from cache");
        }
    }
}
