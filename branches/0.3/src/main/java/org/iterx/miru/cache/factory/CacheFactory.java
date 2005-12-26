/*
  org.iterx.miru.cache.factory.CacheFactory

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

import org.iterx.util.SystemUtils;
import org.iterx.miru.cache.CacheProvider;

public abstract class CacheFactory<K, V> implements CacheProvider<K, V> {

    private static Object cacheFactory;

    public static <K, V> CacheFactory<K, V> getCacheFactory() {

        if(cacheFactory == null) {
            String cls;

            if((cls = SystemUtils.getProperty((CacheFactory.class).getName())) != null) {
                try {
                    cacheFactory = (Class.forName(cls)).newInstance();
                }
                catch(Exception e) {
                    throw new RuntimeException("Failed to create CacheFactory '" + cls + "'.", e);
                }
            }
            else cacheFactory = new MemoryCacheFactory();
        }
        return (CacheFactory<K, V>) cacheFactory;
    }

    public static <K, V> void setCacheFactory(CacheFactory<K, V> cacheFactory) {

        if(cacheFactory == null)
            throw new IllegalArgumentException("cacheFactory == null");
        CacheFactory.cacheFactory = cacheFactory;
    }

}
