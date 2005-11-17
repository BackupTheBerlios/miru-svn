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

public abstract class CacheFactory implements CacheProvider {

    private static CacheFactory cacheFactory;

    public static CacheFactory getCacheFactory() {

        if(cacheFactory == null) {
            String cls;

            if((cls = SystemUtils.getProperty((CacheFactory.class).getName())) != null) {
                try {
                    cacheFactory = (CacheFactory) (Class.forName(cls)).newInstance();
                }
                catch(Exception e) {
                    throw new RuntimeException("Failed to create CacheFactory '" + cls + "'.", e);
                }
            }
            else cacheFactory = new InMemoryCacheFactory();
        }
        return cacheFactory;
    }

    public static void setCacheFactory(CacheFactory cacheFactory) {

        if(cacheFactory == null)
            throw new IllegalArgumentException("cacheFactory == null");
        CacheFactory.cacheFactory = cacheFactory;
    }

}
