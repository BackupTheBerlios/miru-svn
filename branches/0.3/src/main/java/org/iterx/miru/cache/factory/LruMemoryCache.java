/*
  org.iterx.miru.cache.factory.LruMemoryCache

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  Copyright (C)2004-2005 Darren Graves <darren@iterx.org>
  All Rights Reserved.
*/


package org.iterx.miru.cache.factory;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

import org.iterx.miru.cache.Cache;
import org.iterx.miru.cache.CacheListenerAware;
import org.iterx.miru.cache.CacheNotifiable;
import org.iterx.miru.cache.CacheListener;
import org.iterx.util.KeyValue;

public class LruMemoryCache<K, V>
    implements Cache<K, V>, CacheListenerAware, CacheNotifiable {

    private LinkedHashMap<K, V> cache = new LruLinkedHashMap<K, V>();
    private List<CacheListener> cacheListeners = new ArrayList<CacheListener>();

    private int size;


    public LruMemoryCache(int size) {

        if(size < 1) throw new IllegalArgumentException("size < 1");
        this.size = size;
    }

    public V get(K key) {

        notifyListeners(CacheListener.Event.GET, key);
        return cache.get(key);

    }

    public void put(K key, V object) {

        synchronized(cache) {
            cache.put(key, object);
            notifyListeners(CacheListener.Event.PUT,
                            new KeyValue<K, V>(key, object));
        }
    }

    public void remove(K key) {

        synchronized(cache) {
            cache.remove(key);
            notifyListeners(CacheListener.Event.REMOVE, key);
        }
    }

    public Iterator<K> keys() {

        return (cache.keySet()).iterator();
    }

    public void addCacheListener(CacheListener cacheListener) {

        synchronized(cacheListeners){
            cacheListeners.add(cacheListener);
        }
    }

    public void removeCacheListener(CacheListener cacheListener) {

        synchronized(cacheListeners){
            cacheListeners.remove(cacheListener);
        }
    }

    private void notifyListeners(CacheListener.Event event, Object data) {
        int size;

        if((size = cacheListeners.size()) > 0) {
            CacheListener[] listeners;

            listeners = cacheListeners.toArray(new CacheListener[size]);
            for(int i = listeners.length; i-- > 0; ) {
                try {
                    listeners[i].cacheEvent(this, event, data);
                }
                catch(Exception e) {}
            }
        }
    }

    public void cacheNotify(Object source, CacheNotifiable.Event event, Object data) {

        switch(event) {
            case EXPIRE:
                remove((K) data);
                break;
            case CLEAR:
            case DESTROY:
                synchronized(this) {
                    synchronized(cache) {
                        if(cacheListeners.size() != 0) {
                            Object[] keys;

                            keys = (cache.keySet()).toArray();
                            for(int i = keys.length; i-- > 0; ) {
                                remove((K) keys[i]);
                            }
                        }
                        else cache.clear();
                    }
                    if(CacheNotifiable.Event.DESTROY == event) {
                        cacheListeners = null;
                        cache = null;
                    }
                }
                break;
        }
    }

    private class LruLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

        public LruLinkedHashMap() {
            
            super(size, (float) 0.75, true);
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {

            if (size() > size) {
                K key;

                key = eldest.getKey();
                remove(key);
                notifyListeners(CacheListener.Event.REMOVE, key);
            }
            return false;
        }
    }
}