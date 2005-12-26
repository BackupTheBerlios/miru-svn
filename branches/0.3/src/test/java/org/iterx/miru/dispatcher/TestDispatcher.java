/*
  org.iterx.miru.dispatcher.TestDispatcher

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

import junit.framework.TestCase;
import org.iterx.miru.context.factory.ProcessingContextFactory;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.dispatcher.handler.factory.HandlerChainFactoryImpl;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;

public class TestDispatcher extends TestCase {

    private ProcessingContextFactory<RequestContext, ResponseContext> processingContextFactory;
    private HandlerChainMap<RequestContext, ResponseContext> handlerChainMap;

    protected void setUp() {
        HandlerChainFactoryImpl handlerChainFactory;

        handlerChainFactory = new HandlerChainFactoryImpl();

        handlerChainMap = handlerChainFactory.getHandlerChains();
        processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
    }

    protected void tearDown() {

        processingContextFactory = null;
        handlerChainMap = null;
    }

    public void testConstructors() {
        Dispatcher<RequestContext, ResponseContext> dispatcher;

        dispatcher = new Dispatcher<RequestContext, ResponseContext>();
        assertNull(dispatcher.getHandlerChainMap());

        dispatcher = new Dispatcher<RequestContext, ResponseContext>(handlerChainMap);
        assertEquals(handlerChainMap, dispatcher.getHandlerChainMap());
    }
}
