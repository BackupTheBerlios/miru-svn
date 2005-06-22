/*
  org.iterx.miru.dispatcher.handler.TestHandlerMappingImpl

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

import junit.framework.TestCase;

import java.util.Iterator;

import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.handler.MockHandler;
import org.iterx.miru.dispatcher.handler.HandlerMappingImpl;

import org.iterx.miru.dispatcher.interceptor.HandlerInterceptor;


public class TestHandlerMappingImpl extends TestCase {
    
    private HandlerMappingImpl handlerMapping;

    protected void setUp() {

	handlerMapping = new HandlerMappingImpl();
    }

    protected void tearDown() {

	handlerMapping = null;
    }

    public void testConstructors() {
	HandlerMappingImpl handlerMapping;
	
	handlerMapping = new HandlerMappingImpl();
	assertNotNull(handlerMapping);
    }
        
    public void testHandlerAccessors() {
	HandlerInterceptor[] handlerInterceptors;
	HandlerChain chain;
	Iterator chains;
	Handler a, b;

	a = new MockHandler();
	b = new MockHandler();
	handlerInterceptors = new HandlerInterceptor[0];

	handlerMapping.addHandler("a", a);
	assertEquals(a, 
		     (chain = (HandlerChain)
		      handlerMapping.getHandler("a")).getHandler());
	assertEquals(null, chain.getHandlerInterceptors());

	handlerMapping.addHandler("b", b, handlerInterceptors);
	assertEquals(b, 
		     (chain = (HandlerChain) 
		      handlerMapping.getHandler("b")).getHandler());
	assertEquals(handlerInterceptors, chain.getHandlerInterceptors());

	chains = handlerMapping.getHandlers();
	assertEquals(a, 
		     (chain = (HandlerChain) chains.next()).getHandler());
	assertEquals(null, chain.getHandlerInterceptors());
	assertEquals(b, 
		     (chain = (HandlerChain) chains.next()).getHandler());
	assertEquals(handlerInterceptors, chain.getHandlerInterceptors());
	assertFalse(chains.hasNext());

	handlerMapping.removeHandler("a");
	chains = handlerMapping.getHandlers();
	assertEquals(b, 
		     (chain = (HandlerChain) chains.next()).getHandler());
	assertEquals(handlerInterceptors, chain.getHandlerInterceptors());
	assertFalse(chains.hasNext());

	handlerMapping.removeHandler("b");
	chains = handlerMapping.getHandlers();
	assertFalse(chains.hasNext());
    }

}
