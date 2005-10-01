/*
  org.iterx.miru.dispatcher.handler.HandlerChainFactoryImpl

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
package org.iterx.miru.dispatcher.handler;

import java.util.Map;
import java.util.LinkedHashMap;

import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.util.ArrayUtils;

public class HandlerChainFactoryImpl extends HandlerChainFactory
    implements HandlerChainWrapperAware {

    private HandlerAdapter[] handlerAdapters;
    private HandlerChainMap handlerChainMap;
    private Map handlerChains;

    public HandlerChainFactoryImpl() {

        handlerAdapters = new HandlerAdapter[0];
        handlerChains = new LinkedHashMap();
    }

    public HandlerAdapter[] getHandlerAdapters() {

            return handlerAdapters;
    }

    public void setHandlerAdapters(HandlerAdapter[] handlerAdapters) {

        synchronized(this) {
            this.handlerAdapters = handlerAdapters;
        }
    }

    public HandlerAdapter addHandlerAdapter(HandlerAdapter handlerAdapter) {

        synchronized(this) {
            handlerAdapters = (HandlerAdapter[])
                ArrayUtils.add(handlerAdapters, handlerAdapter);
        }
        return handlerAdapter;
    }

    public void removeHandlerAdapter(HandlerAdapter handlerAdapter) {

        synchronized(this) {
            handlerAdapters = (HandlerAdapter[])
                ArrayUtils.remove(handlerAdapters, handlerAdapter);
        }
    }

    public HandlerChainMap getHandlerChains() {

        if(handlerChainMap == null) {
            synchronized(this) {
                if(handlerChainMap == null)
                    handlerChainMap = new HandlerChainMapImpl(new LinkedHashMap(handlerChains));
            }
        }
        return handlerChainMap;
    }

    public HandlerChain createHandlerChain() {

        return new HandlerChainImpl();
    }

    public void addHandlerChain(HandlerChain handler) {
        String name;

        if(handler == null)
            throw new IllegalArgumentException("handler == null");
        if(!(handler instanceof HandlerChainImpl))
            throw new IllegalArgumentException
                ("Unsupported HandlerChain implementation [" + handler.getClass() + "].");

        synchronized(this) {
            if((name = handler.getId()) != null) handlerChains.put(name, handler);
            else handlerChains.put(handler, handler);
        }
    }

    public void removeHandlerChain(HandlerChain handler) {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");

        synchronized(this) {
            (handlerChains.values()).remove(handler);
        }
    }

    public HandlerChainWrapper assignHandlerChainWrapper(Object object) {
        HandlerChainWrapper wrapper;

        wrapper = new HandlerChainWrapperImpl(this);
        wrapper.setWrappedInstance(object);
        return wrapper;
    }

    public void recycleHandlerChainWrapper(HandlerChainWrapper wrapper) {

        wrapper.setWrappedInstance(null);        
    }


}
