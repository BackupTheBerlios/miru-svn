/*
  org.iterx.miru.dispatcher.context.DispatcherApplicationContextImpl

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
package org.iterx.miru.dispatcher.context;

import org.iterx.miru.dispatcher.handler.factory.HandlerChainFactory;
import org.iterx.miru.context.ApplicationContextImpl;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.bean.BeanWrapper;

public class DispatcherApplicationContextImpl<S extends RequestContext, T extends ResponseContext> extends ApplicationContextImpl
    implements DispatcherApplicationContext<S, T>, BeanWrapperAware {

    private HandlerChainFactory<S, T> handlerChainFactory;

    protected DispatcherApplicationContextImpl() {}

    public DispatcherApplicationContextImpl(BeanProvider beanProvider) {

        super(beanProvider);
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");

    }

    public DispatcherApplicationContextImpl(BeanProvider beanProvider,
                                            ApplicationContext parent) {
        super(beanProvider, parent);
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");
    }

    public HandlerChainFactory<S, T> getHandlerChainFactory() {

        if(handlerChainFactory == null) {
            if((handlerChainFactory =
                (HandlerChainFactory<S, T>) getBeanOfType(HandlerChainFactory.class)) == null)
                handlerChainFactory = HandlerChainFactory.getHandlerChainFactory();
            handlerChainFactory.setBeanProvider(this);
        }
        return handlerChainFactory;
    }

    public void setHandlerChainFactory(HandlerChainFactory<? extends S, ? extends T> handlerChainFactory) {

        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");

        this.handlerChainFactory = (HandlerChainFactory<S, T>) handlerChainFactory;
    }


    public BeanWrapper assignBeanWrapper(Object object) {

        return ((BeanWrapperAware) beanProvider).assignBeanWrapper(object);
    }

    public void recycleBeanWrapper(BeanWrapper wrapper) {

        ((BeanWrapperAware) beanProvider).recycleBeanWrapper(wrapper);
    }
}
