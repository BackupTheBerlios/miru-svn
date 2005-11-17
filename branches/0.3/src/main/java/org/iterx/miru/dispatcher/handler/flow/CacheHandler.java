/*
  org.iterx.miru.dispatcher.handler.flow.CacheHandler

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

package org.iterx.miru.dispatcher.handler.flow;

import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.cache.Cache;

public class CacheHandler implements FlowHandler {

    private Handler handler;
    private Cache cache;


    public Cache getCache() {

        return cache;
    }

    public void setCache(Cache cache) {

        if(cache == null)
            throw new IllegalArgumentException("cache == null");
        this.cache = cache;
    }


    public Handler getHandler()  {

        return handler;
    }

    public void setHandler(Handler handler)  {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");
        this.handler = handler;
    }

    public Object[] getMatches(ProcessingContext context) {

        return new Object[0];
    }

    public boolean hasMatches(ProcessingContext context) {

        return false;
    }

    public int execute(ProcessingContext processingContext) {
        //get matches and use as key for cache

        return 0;
    }


}
