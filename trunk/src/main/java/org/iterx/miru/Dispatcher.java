/*
  org.iterx.miru.Dispatcher;

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

package org.iterx.miru;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.ResponseContext;

import org.iterx.miru.handler.Handler;
import org.iterx.miru.handler.HandlerMapping;
import org.iterx.miru.handler.HandlerChain;
import org.iterx.miru.interceptor.HandlerInterceptor;
import org.iterx.miru.resolver.HandlerResolver;

public class Dispatcher implements ApplicationContextAware {

    public static final int ERROR   = -1;
    public static final int OK      = 0;
    public static final int DECLINE = 1;
    public static final int DONE    = 2;

    protected final Log logger = LogFactory.getLog(Dispatcher.class);

    private HandlerResolver handlerResolver;
    private HandlerMapping handlerMapping;

    public Dispatcher() {}

    public Dispatcher(ApplicationContext applicationContext) {
	
	setApplicationContext(applicationContext);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {

	handlerMapping = 
	    (applicationContext.getHandlerMappingFactory()).getHandlerMapping();
    }

    public HandlerResolver getHandlerResolver() {

	return handlerResolver;
    }

    public void setHandlerResolver(HandlerResolver handlerResolver) {

	if(logger.isDebugEnabled())
	    logger.debug("Setting HandlerResolver [" + handlerResolver + "]");
	this.handlerResolver = handlerResolver;	
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
		Handler handler;
		int j;
		
		j = 0;
		chain = (HandlerChain) chains.next();
		if((interceptors = chain.getHandlerInterceptors()) != null) {
		    
		    for(; ++j < interceptors.length; ) {
			HandlerInterceptor interceptor;
			
			interceptor = interceptors[j];
			if(logger.isDebugEnabled())
			    logger.debug("Invoking HandlerInterceptor [" +
					 interceptor + "]");
			if(!interceptor.preHandle(processingContext)) break;
		    }
		    if(j != interceptors.length) continue;
		}
		
		if((handler = chain.getHandler()) instanceof Handler) {
		    if(logger.isDebugEnabled())
			logger.debug("Invoking Handler [" + 
				     handler + "]");
		    status = handler.handle(processingContext);
		}
		else (processingContext.getResponseContext()).setStatus
			 (ResponseContext.NOT_IMPLEMENTED);
		
		if(interceptors != null) {
		    for(; j-- > 0;) {
			interceptors[j].postHandle(processingContext);
		    }
		}
	    }	    
	}
	return status;
    }

}
