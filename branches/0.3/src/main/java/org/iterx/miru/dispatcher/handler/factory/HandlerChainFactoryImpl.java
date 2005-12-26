/*
  org.iterx.miru.dispatcher.handler.factory.HandlerChainFactoryImpl

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
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;

import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.dispatcher.handler.HandlerWrapperAware;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.handler.HandlerWrapper;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.util.ArrayUtils;

public class HandlerChainFactoryImpl<S extends RequestContext, T extends ResponseContext> extends HandlerChainFactory<S, T>
    implements HandlerWrapperAware<S, T> {

    private HandlerAdapter<S, T>[] handlerAdapters = (HandlerAdapter<S, T>[]) new HandlerAdapter[0];
    private HandlerChainMap<S, T> handlerChainMap;

    private Map<String, HandlerChain<? extends S, ? extends T>> handlerChains = new LinkedHashMap<String, HandlerChain<? extends S, ? extends T>>(1);


    public HandlerChainFactoryImpl() {}

    public HandlerChainFactoryImpl(BeanProvider beanProvider){

        super(beanProvider);
    }

    public HandlerAdapter<S, T>[] getHandlerAdapters() {

            return handlerAdapters;
    }

    public void setHandlerAdapters(List<HandlerAdapter<? extends S, ? extends T>> handlerAdapters) {

        synchronized(this) {
            this.handlerAdapters = handlerAdapters.toArray(this.handlerAdapters);
        }

    }

    public void addHandlerAdapter(HandlerAdapter<? extends S, ? extends T> handlerAdapter) {

        synchronized(this) {
            handlerAdapters = (HandlerAdapter<S, T>[])
                ArrayUtils.add(handlerAdapters, handlerAdapter);
        }
    }

    public void removeHandlerAdapter(HandlerAdapter<? extends S, ? extends T> handlerAdapter) {

        synchronized(this) {
            handlerAdapters = (HandlerAdapter<S, T>[])
                ArrayUtils.remove(handlerAdapters, handlerAdapter);
        }
    }

    public HandlerChain<S, T> createHandlerChain() {

        return new HandlerChainImpl<S, T>();
    }

    public void addHandlerChain(HandlerChain<? extends S, ? extends T> handlerChain) {

        if(handlerChain == null)
            throw new IllegalArgumentException("handlerChain == null");
        if(!(handlerChain instanceof HandlerChainImpl))
            throw new IllegalArgumentException
                ("Unsupported HandlerChain implementation [" + handlerChain.getClass() + "].");

        synchronized(this) {
            handlerChains.put(handlerChain.getId(), handlerChain);
        }
    }

    public HandlerChainMap<S, T> getHandlerChains() {

        if(handlerChainMap == null) {
            synchronized(this) {
                if(handlerChainMap == null)
                    handlerChainMap = new HandlerChainMapImpl<S, T>(handlerChains);
            }
        }
        return handlerChainMap;
    }

    public void setHandlerChains(HandlerChainMap<? extends S, ? extends T> handlerChainMap) {

        if(handlerChainMap == null)
            throw new IllegalArgumentException("handlerChainMap == null");

        synchronized(this) {
            handlerChains.clear();

            for(Iterator iterator = handlerChainMap.iterator();
                iterator.hasNext(); ) {
                addHandlerChain((HandlerChain<S ,T>) iterator.next());
            }
        }
    }

    public void removeHandlerChain(HandlerChain handler) {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");

        synchronized(this) {
            (handlerChains.values()).remove(handler);
        }
    }

    public HandlerWrapper<S, T> assignHandlerWrapper(Object object) {
        assert (getBeanProvider() != null) : "beanProvider == null";
        HandlerWrapperImpl<S, T> wrapper;

        wrapper = new HandlerWrapperImpl<S, T>(this, getBeanProvider());
        wrapper.setWrappedInstance(object);
        return wrapper;
    }

    public void recycleHandlerWrapper(HandlerWrapper<S, T> wrapper) {

        assert (wrapper == null ||
                wrapper instanceof HandlerWrapperImpl) : "Invalid instance.";

        wrapper.setWrappedInstance(null);
    }

}
