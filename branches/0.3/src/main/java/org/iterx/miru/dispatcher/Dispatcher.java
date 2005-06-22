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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.util.ArrayUtils;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;

import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.handler.HandlerMapping;
import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.interceptor.HandlerInterceptor;
import org.iterx.miru.dispatcher.resolver.HandlerResolver;

public class Dispatcher implements ApplicationContextAware {

    public static final int ERROR   = -1;
    public static final int OK      = 0;
    public static final int DECLINE = 1;
    public static final int DONE    = 2;

    protected final Log logger = LogFactory.getLog(Dispatcher.class);

    private HandlerResolver handlerResolver;
    private HandlerMapping handlerMapping;
    private HandlerAdapter[] handlerAdapters;


    {
        handlerAdapters = new HandlerAdapter[0];
    }
    public Dispatcher() {}

    public Dispatcher(HandlerMapping handlerMapping) {

        if(handlerMapping == null)
            throw new IllegalArgumentException("handlerMapping == null");
        this.handlerMapping = handlerMapping;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        if(applicationContext == null)
            throw new IllegalArgumentException("applicationContext == null");

    handlerMapping =
    (applicationContext.getHandlerMappingFactory()).getHandlerMapping();
    }

    public HandlerMapping getHandlerMapping() {

        return handlerMapping;
    }

    public void setHandlerMapping(HandlerMapping handlerMapping) {

        if(handlerMapping == null)
            throw new IllegalArgumentException("handlerMapping == null");
        this.handlerMapping = handlerMapping;
    }

    public HandlerResolver getHandlerResolver() {

    return handlerResolver;
    }

    public void setHandlerResolver(HandlerResolver handlerResolver) {

    this.handlerResolver = handlerResolver;
    }


    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {

        handlerAdapters = (HandlerAdapter[])
            ArrayUtils.add(handlerAdapters, handlerAdapter);
    }

    public HandlerAdapter[] getHandlerAdapters() {

        return handlerAdapters;
    }

    public void setHandlerAdapters(HandlerAdapter[] handlerAdapters) {

        this.handlerAdapters = handlerAdapters;
    }

    public void removeHandlerAdapter(HandlerAdapter handlerAdapter) {

        handlerAdapters = (HandlerAdapter[])
            ArrayUtils.remove(handlerAdapters, handlerAdapter);
    }

    public int dispatch(ProcessingContext processingContext) {
    assert (processingContext != null) : "processingContext == null";

    Iterator chains;
    int status;

    status = DECLINE;
    if((chains = ((handlerResolver == null)?
                  handlerMapping.getHandlers() :
                  handlerResolver.resolve(handlerMapping,
                                          processingContext))) != null) {
        while(chains.hasNext()) {
        HandlerInterceptor[] interceptors;
        HandlerChain chain;
        Object handler;
        int i;

        i = -1;
        chain = (HandlerChain) chains.next();
        if((interceptors = chain.getHandlerInterceptors()) != null) {

            for(; ++i < interceptors.length; ) {
            HandlerInterceptor interceptor;

            interceptor = interceptors[i];
            if(logger.isDebugEnabled())
                logger.debug("Invoking HandlerInterceptor [" +
                             interceptor + "]");
            if(!interceptor.preHandle(processingContext)) break;
            }
            if(i != interceptors.length) continue;
        }

        if((handler = chain.getHandler()) instanceof Handler) {
            if(logger.isDebugEnabled())
            logger.debug("Invoking Handler [" +
                         handler + "]");
            status = ((Handler) handler).handle(processingContext);
        }
        else {
                    int j;

                    j = -1;
                    for(; ++j < handlerAdapters.length; ) {
                        HandlerAdapter handlerAdapter;

                        handlerAdapter = handlerAdapters[j];
                        if(handlerAdapter.supports(handler)) {
                            if(logger.isDebugEnabled())
                                logger.debug("Invoking HandlerAdapter [" +
                                             handlerAdapter + "]");
                            status = handlerAdapter.handle(processingContext,
                                                           handler);
                            break;
                        }
                    }
                    if(j == handlerAdapters.length)
                        throw new RuntimeException("Unsupported Handler.");
        }

        if(interceptors != null) {
            for(; i-- > 0;) {
            interceptors[i].postHandle(processingContext);
            }
        }
                return status;
        }
    }
    return status;
    }

}
