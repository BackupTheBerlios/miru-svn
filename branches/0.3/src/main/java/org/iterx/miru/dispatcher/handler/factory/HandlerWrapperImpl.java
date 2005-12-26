/*
  org.iterx.miru.dispatcher.handler.factory.HandlerWrapperImpl

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
import java.util.List;
import java.util.ArrayList;

import org.iterx.miru.dispatcher.handler.HandlerWrapper;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;

public class HandlerWrapperImpl<S extends RequestContext, T extends ResponseContext> implements HandlerWrapper<S, T> {

    private HandlerChainFactory<S, T> handlerChainFactory;
    private BeanProvider beanProvider;
    private BeanWrapper beanWrapper;

    public HandlerWrapperImpl(HandlerChainFactory<? extends S, ? extends T> handlerChainFactory,
                              BeanProvider beanProvider) {
        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");
        if(beanProvider == null)
            throw new IllegalArgumentException("beanProvider == null");
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");

        this.handlerChainFactory = (HandlerChainFactory<S, T>) handlerChainFactory;
        this.beanProvider = beanProvider;
    }

    public Object getWrappedInstance() {

        return ((beanWrapper != null)?
                beanWrapper.getWrappedInstance() : null);
    }

    public void setWrappedInstance(Object object) {
        BeanWrapperAware beanWrapperAware;

        beanWrapperAware =(BeanWrapperAware) beanProvider;

        if(beanWrapper != null) {
            beanWrapperAware.recycleBeanWrapper(beanWrapper);
            beanWrapper = null;
        }
        if(object != null)
            beanWrapper = beanWrapperAware.assignBeanWrapper(object);
    }

    public Object getValue(String property) {

        return beanWrapper.getValue(property);
    }

    public void setValue(String property, Object value) {

        beanWrapper.setValue(property, value);
    }

    public void setValues(Map<String, Object> map) {

        beanWrapper.setValues(map);
    }

    public void setHandler(Object object) {

        if(object != null && !(object instanceof Handler)) {
            for(HandlerAdapter<S, T> adapter : handlerChainFactory.getHandlerAdapters()) {
                if(adapter.supports(object)) {
                    object = new HandlerProxy<S, T>(adapter, object);
                    break;
                }
            }
        }
        beanWrapper.setValue("handler", object);
    }

    public void setHandlers(List<Object> objects) {
        List<Handler<S, T>> handlers;

        handlers = null;
        if(objects != null) {
            HandlerAdapter<S, T>[] adapters;
            Handler<S, T> handler;

            handlers = new ArrayList<Handler<S, T>>();
            adapters = handlerChainFactory.getHandlerAdapters();
            for(Object object : objects) {
                OUTER: if(!(object instanceof Handler)) {
                    for(HandlerAdapter<S, T> adapter : adapters) {
                        if(adapter.supports(object)) {
                            handler = new HandlerProxy<S, T>(adapter, object);
                            break OUTER;
                        }
                    }
                    throw new IllegalArgumentException("Unsupported Handler [" + object + "].");
                }
                else handler = (Handler<S, T>) object;
                handlers.add(handler);
            }
        }
        beanWrapper.setValue("handlers", handlers);
    }


    private static class HandlerProxy<S extends RequestContext, T extends ResponseContext> implements Handler<S, T> {

        private HandlerAdapter<S, T> adapter;
        private Object object;

        private HandlerProxy(HandlerAdapter<? extends S, ? extends T> adapter, Object handler) {

            this.adapter = (HandlerAdapter<S, T>) adapter;
            this.object = handler;
        }

        public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {

            return adapter.execute(processingContext, object);
        }

    }
}
