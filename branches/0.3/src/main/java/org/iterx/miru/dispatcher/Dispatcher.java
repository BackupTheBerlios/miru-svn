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

import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.resolver.HandlerResolver;

public class Dispatcher {

    public static final int ERROR   = -1;
    public static final int OK      = 0;
    public static final int DECLINE = 1;
    public static final int DONE    = 2;


    private HandlerResolver handlerResolver;
    private HandlerChainMap handlerChainMap;

    public Dispatcher() {}

    public Dispatcher(HandlerChainMap handlerChainMap) {

        if(handlerChainMap == null)
            throw new IllegalArgumentException("handlerChainMap == null");
        this.handlerChainMap = handlerChainMap;
    }

    public HandlerChainMap getHandlerChainMap() {

        return handlerChainMap;
    }

    public void setHandlerChainMap(HandlerChainMap handlerChainMap) {

        if(handlerChainMap == null)
            throw new IllegalArgumentException("handlerChainMap == null");
        this.handlerChainMap = handlerChainMap;
    }

    public HandlerResolver getHandlerResolver() {

        return handlerResolver;
    }

    public void setHandlerResolver(HandlerResolver handlerResolver) {

        this.handlerResolver = handlerResolver;
    }


    public int dispatch(ProcessingContext processingContext) {
        assert (handlerChainMap != null) : "handlerChainMap == null";
        assert (processingContext != null) : "processingContext == null";
        Iterator chains;
        int status;

        status = DECLINE;
        if((chains = ((handlerResolver == null)?
                      handlerChainMap.iterator() :
                      handlerResolver.resolve(handlerChainMap,
                                              processingContext))) != null) {
            while(chains.hasNext()) {
                HandlerChain handlerChain;
                Object[] matches;

                handlerChain = (HandlerChain) chains.next();
                if((matches = handlerChain.getMatches(processingContext)) != null) {

                    if((status = handlerChain.execute(processingContext)) != DECLINE)
                        break; 
                }
            }
        }
        return status;
    }

}
