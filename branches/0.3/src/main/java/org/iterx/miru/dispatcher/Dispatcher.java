/*
  org.iterx.miru.dispatcher.Dispatcher

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
package org.iterx.miru.dispatcher;

import java.util.Iterator;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.resolver.HandlerResolver;
import org.iterx.miru.matcher.Matches;

public class Dispatcher<S extends RequestContext, T extends ResponseContext> {

    public static final int ERROR   = -1;
    public static final int OK      = 0;
    public static final int DECLINE = 1;
    public static final int DONE    = 2;

    private HandlerChainMap<S, T> handlerChainMap;
    private HandlerResolver<S, T> handlerResolver;

    public Dispatcher() {}

    public Dispatcher(HandlerChainMap<? extends S, ? extends T> handlerChainMap) {

        if(handlerChainMap == null)
            throw new IllegalArgumentException("handlerChainMap == null");
        this.handlerChainMap = (HandlerChainMap<S, T>) handlerChainMap;
    }

    public HandlerChainMap<S, T>  getHandlerChainMap() {

        return handlerChainMap;
    }

    public void setHandlerChainMap(HandlerChainMap<? extends S, ? extends T> handlerChainMap) {

        if(handlerChainMap == null)
            throw new IllegalArgumentException("handlerChainMap == null");
        this.handlerChainMap = (HandlerChainMap<S, T>) handlerChainMap;
    }

    public HandlerResolver<S, T> getHandlerResolver() {

        return handlerResolver;
    }

    public  void setHandlerResolver(HandlerResolver<? extends S, ? extends T> handlerResolver) {

        this.handlerResolver = (HandlerResolver<S, T>) handlerResolver;
    }


    public  int dispatch(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (handlerChainMap != null) : "handlerChainMap == null";
        Iterator<HandlerChain<S, T>> chains;
        int status;

        status = DECLINE;

        if((chains = ((handlerResolver == null)?
                      handlerChainMap.iterator() :
                      handlerResolver.resolve(handlerChainMap,
                                              processingContext))) != null) {
            while(chains.hasNext()) {
                HandlerChain<S, T> handlerChain;
                Matches matches;

                handlerChain = chains.next();
                if((matches = handlerChain.getMatches(processingContext)) != null) {
                    processingContext.setAttribute(ProcessingContext.MATCHES_ATTRIBUTE, matches);
                    if((status = handlerChain.execute(processingContext)) != DECLINE)
                        break;
                }
            }
        }
        return status;
    }


}


