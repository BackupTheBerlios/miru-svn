/*
  org.iterx.miru.dispatcher.handler.factory.HandlerChainFactory

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

import org.iterx.miru.dispatcher.handler.HandlerChainProvider;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.util.SystemUtils;

public abstract class HandlerChainFactory<S extends RequestContext, T extends ResponseContext> implements HandlerChainProvider<S, T> {

    private static Object handlerChainFactory;

    private BeanProvider beanProvider;

    public HandlerChainFactory() {}

    public HandlerChainFactory(BeanProvider beanProvider) {

        setBeanProvider(beanProvider);
    }

    public BeanProvider getBeanProvider() {

        return beanProvider;
    }

    public void setBeanProvider(BeanProvider beanProvider) {

        if(beanProvider == null)
            throw new IllegalArgumentException("beanProvider == null");
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");

        synchronized(this)  {
            this.beanProvider = beanProvider;
        }
    }

    public static <S extends RequestContext, T extends ResponseContext>  HandlerChainFactory<S, T> getHandlerChainFactory() {

        if(handlerChainFactory == null) {

            String cls;

            if((cls = SystemUtils.getProperty((HandlerChainFactory.class).getName())) != null) {
                try {
                    handlerChainFactory = (Class.forName(cls)).newInstance();
                }
                catch(Exception e) {
                    throw new RuntimeException
                        ("Failed to create HandlerChainFactory '" + cls + "'.", e);
                }
            }
            else handlerChainFactory = new XmlHandlerChainFactory();
        }
        return (HandlerChainFactory<S, T>) handlerChainFactory;
    }

    public static <S extends RequestContext, T extends ResponseContext> void setHandlerChainFactory(HandlerChainFactory<? extends S, ? extends T> handlerChainFactory) {

        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");

        HandlerChainFactory.handlerChainFactory = handlerChainFactory;

    }
}
