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
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;

public class HandlerWrapperImpl implements HandlerWrapper {

    private HandlerChainFactory handlerChainFactory;
    private BeanProvider beanProvider;
    private BeanWrapper beanWrapper;

    public HandlerWrapperImpl(HandlerChainFactory handlerChainFactory,
                              BeanProvider beanProvider) {
        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");
        if(beanProvider == null)
            throw new IllegalArgumentException("beanProvider == null");
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");

        this.handlerChainFactory = handlerChainFactory;
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

    public void setValues(Map map) {

        beanWrapper.setValues(map);
    }

    public void setHandler(Object handler) {

        if(handler != null &&
           !(handler instanceof Handler)) {
            HandlerAdapter[] adapters;
            adapters = handlerChainFactory.getHandlerAdapters();

            for(int i = 0; i < adapters.length; i++) {
                if(adapters[i].supports(handler)) {
                    handler = new HandlerProxy(adapters[i], handler);
                    break;
                }
            }
        }
        beanWrapper.setValue("handler", handler);
    }

    public void setHandlers(Object handlers) {
        Object[] values;

        values = null;
        if(handlers instanceof Object[]) values = (Object[]) handlers;
        else if(handlers instanceof List)  values = ((List) handlers).toArray();
        if(values != null) {

            HandlerAdapter[] adapters;
            List array;
            Object value;

            array = new ArrayList();
            adapters = handlerChainFactory.getHandlerAdapters();
            for(int i = 0; i < values.length; i++) {
                value = values[i];
                if(!(value instanceof Handler)) {
                    for(int j = 0; j < adapters.length; j++) {
                        if(adapters[j].supports(value)) {
                            value = new HandlerProxy(adapters[j], value);
                            break;
                        }
                    }
                }
                array.add(value);
            }
            handlers = array;
        }
        beanWrapper.setValue("handlers", handlers);
    }


    private static class HandlerProxy implements Handler {

        private HandlerAdapter adapter;
        private Object handler;

        private HandlerProxy(HandlerAdapter adapter, Object handler) {

            this.adapter = adapter;
            this.handler = handler;
        }

        public int execute(ProcessingContext processingContext) {

            return adapter.execute(processingContext, handler);
        }

    }
}
