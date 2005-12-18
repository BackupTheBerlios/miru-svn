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

import org.iterx.miru.cache.Cache;
import org.iterx.miru.cache.CacheNotifiable;

public class LruMemoryCacheFactory extends CacheFactory {

    private static final int DEFAULT_SIZE = 128;

    private HashMap caches = new HashMap();
    private int size = DEFAULT_SIZE;

    public int getSize() {

        return size;
    }

    public void setSize(int size) {

        this.size = size;
    }

    public Cache getCache(String name) {

        synchronized(caches) {
            LruMemoryCache cache;

            if((cache = (LruMemoryCache) caches.get(name)) == null) {
                cache = new LruMemoryCache(size);
                caches.put(name, cache);
            }
            return cache;
        }
    }

    public void recycleCache(String name) {

        synchronized(caches) {
            LruMemoryCache cache;

            cache = (LruMemoryCache) caches.remove(name);
            cache.cacheNotify(this, CacheNotifiable.Event.DESTROY, null);
        }
    }


}