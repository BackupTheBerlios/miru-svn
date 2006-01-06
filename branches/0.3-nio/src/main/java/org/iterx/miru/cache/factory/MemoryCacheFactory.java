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

public class MemoryCacheFactory<K, V> extends CacheFactory<K, V> {

    private Map<String, MemoryCache<K, V>> caches = new HashMap<String, MemoryCache<K, V>>();

    public MemoryCache<K, V> getCache(String name) {

        synchronized(caches) {
            MemoryCache<K, V> cache;

            if((cache = caches.get(name)) == null) {
                cache = new MemoryCache<K, V>();
                caches.put(name, cache);
            }
            return cache;
        }
    }

    public void recycleCache(String name) {

        synchronized(caches) {
            MemoryCache<K, V> cache;

            cache = caches.remove(name);
            cache.cacheNotify(this, CacheNotifiable.Event.DESTROY, null);
        }
    }

}
