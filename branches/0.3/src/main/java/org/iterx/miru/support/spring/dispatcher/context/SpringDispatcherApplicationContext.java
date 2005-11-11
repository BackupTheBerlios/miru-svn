/*
  org.iterx.miru.support.spring.dispatcher.context.pringDispatcherApplicationContext

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

package org.iterx.miru.support.spring.dispatcher.context;

import org.iterx.miru.support.spring.context.SpringApplicationContext;
import org.iterx.miru.dispatcher.context.DispatcherApplicationContext;
import org.iterx.miru.dispatcher.handler.HandlerChainFactory;
import org.iterx.miru.bean.BeanException;
import org.iterx.miru.bean.BeanFactory;
import org.iterx.miru.context.ApplicationContext;

public class SpringDispatcherApplicationContext extends SpringApplicationContext
    implements DispatcherApplicationContext {

    private HandlerChainFactory handlerMappingFactory;

    public SpringDispatcherApplicationContext() {}

    public SpringDispatcherApplicationContext(ApplicationContext parent) {

        super(parent);

    }

    public SpringDispatcherApplicationContext(BeanFactory beanFactory) {

        super(beanFactory);
    }

    public SpringDispatcherApplicationContext
        (org.springframework.beans.factory.BeanFactory beanFactory) {
        super(beanFactory);
    }


    public HandlerChainFactory getHandlerChainFactory() {

        if(handlerMappingFactory == null) {
            if((handlerMappingFactory =
                (HandlerChainFactory) getBeanOfType(HandlerChainFactory.class)) == null)
                throw new BeanException();
        }
        return handlerMappingFactory;
    }

}
