/*
  org.iterx.miru.cache.factory.MemoryCacheFactory

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

import java.util.HashMap;
import java.util.Map;

import org.iterx.miru.cache.CacheNotifiable;

public class LruMemoryCacheFactory<K, V> extends CacheFactory<K, V> {

    private static final int DEFAULT_SIZE = 128;

    private Map<String, LruMemoryCache<K, V>> caches = new HashMap<String, LruMemoryCache<K, V>>();
    private int size = DEFAULT_SIZE;

    public int getSize() {

        return size;
    }

    public void setSize(int size) {

        this.size = size;
    }

    public LruMemoryCache<K, V> getCache(String name) {

        synchronized(caches) {
            LruMemoryCache<K, V> cache;

            if((cache = caches.get(name)) == null) {
                cache = new LruMemoryCache<K, V>(size);
                caches.put(name, cache);
            }
            return cache;
        }
    }

    public void recycleCache(String name) {

        synchronized(caches) {
            LruMemoryCache<K, V> cache;

            cache = caches.remove(name);
            cache.cacheNotify(this, CacheNotifiable.Event.DESTROY, null);
        }
    }

}