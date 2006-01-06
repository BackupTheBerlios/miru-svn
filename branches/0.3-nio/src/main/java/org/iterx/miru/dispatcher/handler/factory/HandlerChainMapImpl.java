/*
  org.iterx.miru.dispatcher.handler.factory.HandlerChainImpl

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
package org.iterx.miru.dispatcher.handler.factory;

import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.handler.HandlerChain;

public class HandlerChainMapImpl<S extends RequestContext,  T extends ResponseContext> implements HandlerChainMap<S, T> {
    private Map<String, HandlerChain<S, T>> handlerChains;

    public HandlerChainMapImpl(Map<String, HandlerChain<? extends S, ? extends T>> handlerChains) {

        this.handlerChains = new HashMap<String, HandlerChain<S, T>>(handlerChains.size());

        for(Map.Entry<String, HandlerChain<? extends S, ? extends T>> entry : handlerChains.entrySet()) {

            (this.handlerChains).put(entry.getKey(),
                                     (HandlerChain<S, T>) entry.getValue()); // force upcasting
        }
    }

    public HandlerChain<S, T> get(String id) {

        return ((id != null)? handlerChains.get(id) : null);
    }

    public Iterator<HandlerChain<S, T>> iterator() {

        return (handlerChains.values()).iterator();
    }
}