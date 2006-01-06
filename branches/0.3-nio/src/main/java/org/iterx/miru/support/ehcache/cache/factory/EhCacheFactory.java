/*
  org.iterx.miru.support.ehcache.cache.factory.EhCacheFactory

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

package org.iterx.miru.support.ehcache.cache.factory;

import java.io.IOException;
import java.io.Serializable;

import org.iterx.miru.cache.factory.CacheFactory;
import org.iterx.miru.cache.Cache;
import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.stream.StreamSource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.CacheException;

public class EhCacheFactory<K extends Serializable, V extends Serializable>  extends CacheFactory<K, V>  implements Loadable {

    private CacheManager cacheManager;

    public Cache<K, V> getCache(String name) {
        assert (cacheManager != null) : "cacheManager == null";

        return new EhCache<K, V>(cacheManager.getCache(name));
    }

    public void recycleCache(String name) {
        assert (cacheManager != null) : "cacheManager == null";

        cacheManager.removeCache(name);
    }

    public void load(Resource resource) throws IOException {

        if(resource == null)
            throw new IllegalArgumentException("stream == null");
        if(!(resource instanceof StreamSource))
            throw new IllegalArgumentException("stream is not a StreamSource.");

        try {
            cacheManager = new CacheManager(((StreamSource) resource).getInputStream());
        }
        catch(CacheException e) {
            throw new RuntimeException("Failed to initialise EhCache.", e);
        }
    }

}
