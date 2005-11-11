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

import org.iterx.miru.dispatcher.handler.HandlerChainFactory;
import org.iterx.miru.context.ApplicationContextImpl;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.bean.BeanProvider;

public class DispatcherApplicationContextImpl extends ApplicationContextImpl
    implements DispatcherApplicationContext {

    private HandlerChainFactory handlerChainFactory;

    protected DispatcherApplicationContextImpl() {}

    public DispatcherApplicationContextImpl(BeanProvider beanProvider) {

        super(beanProvider);
    }

    public DispatcherApplicationContextImpl(BeanProvider beanProvider,
                                            ApplicationContext parent) {
        super(beanProvider, parent);
    }


    public HandlerChainFactory getHandlerChainFactory() {

        if(handlerChainFactory == null) {
            if((handlerChainFactory =
                (HandlerChainFactory) getBeanOfType(HandlerChainFactory.class)) == null)
            handlerChainFactory = HandlerChainFactory.getHandlerChainFactory();
        }
        return handlerChainFactory;
    }

    public void setHandlerChainFactory(HandlerChainFactory handlerChainFactory) {

        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");

        this.handlerChainFactory = handlerChainFactory;
    }

}
